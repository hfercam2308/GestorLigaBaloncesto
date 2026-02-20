package com.universidad.ligabasket.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.universidad.ligabasket.model.CondicionesMeteorologicas;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

// Servicio para conectar con la API de OpenWeather (Clima actual y pronósticos)
@Service
public class WeatherService {

    private final RestTemplate restTemplate;

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}")
    private String apiUrl;

    @Value("${weather.api.forecast.url}")
    private String forecastUrl;

    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Obtiene clima por coordenadas, decidiendo si usar 'actual' o 'pronóstico'
    // según la fecha
    public CondicionesMeteorologicas obtenerClimaPorCoordenadas(double lat, double lon, LocalDate fecha, String hora) {
        if (esFechaEnRangoPronostico(fecha)) {
            String url = forecastUrl + "?lat=" + lat + "&lon=" + lon + "&appid=" + apiKey + "&units=metric&lang=es";
            return ejecutarPeticionForecast(url, fecha, hora);
        }
        String url = apiUrl + "?lat=" + lat + "&lon=" + lon + "&appid=" + apiKey + "&units=metric&lang=es";
        return ejecutarPeticionClima(url);
    }

    // Obtiene el clima usando el nombre de la ciudad como criterio de búsqueda
    public CondicionesMeteorologicas obtenerClimaPorCiudad(String ciudad, LocalDate fecha, String hora) {
        if (ciudad == null || ciudad.isEmpty()) {
            return new CondicionesMeteorologicas(20, "No disponible", 50, "01d");
        }
        if (esFechaEnRangoPronostico(fecha)) {
            String url = forecastUrl + "?q=" + ciudad + "&appid=" + apiKey + "&units=metric&lang=es";
            return ejecutarPeticionForecast(url, fecha, hora);
        }
        String url = apiUrl + "?q=" + ciudad + "&appid=" + apiKey + "&units=metric&lang=es";
        return ejecutarPeticionClima(url);
    }

    // Verifica si la fecha está dentro de los próximos 5 días para permitir el
    // pronóstico
    private boolean esFechaEnRangoPronostico(LocalDate fecha) {
        if (fecha == null)
            return false;
        LocalDate hoy = LocalDate.now();
        return !fecha.isBefore(hoy) && fecha.isBefore(hoy.plusDays(5));
    }

    // Llama a la API de clima actual y mapea la respuesta al modelo interno
    private CondicionesMeteorologicas ejecutarPeticionClima(String url) {
        try {
            WeatherResponse response = restTemplate.getForObject(url, WeatherResponse.class);
            if (response != null && response.getMain() != null && response.getWeather() != null
                    && !response.getWeather().isEmpty()) {
                return new CondicionesMeteorologicas(
                        (int) Math.round(response.getMain().getTemp()),
                        response.getWeather().get(0).getDescription(),
                        response.getMain().getHumidity(),
                        response.getWeather().get(0).getIcon());
            }
        } catch (Exception e) {
            System.err.println("Error al obtener clima actual: " + e.getMessage());
        }
        return new CondicionesMeteorologicas(20, "No disponible", 50, "01d"); // Fallback neutro
    }

    // Obtiene el pronóstico de 5 días y busca el tramo de 3 horas que más se ajusta
    // al partido
    private CondicionesMeteorologicas ejecutarPeticionForecast(String url, LocalDate fecha, String hora) {
        try {
            ForecastResponse response = restTemplate.getForObject(url, ForecastResponse.class);
            if (response != null && response.getList() != null && !response.getList().isEmpty()) {
                String targetDateStr = fecha.toString();

                ForecastItem bestMatch = null;
                for (ForecastItem item : response.getList()) {
                    if (item.getDt_txt() != null && item.getDt_txt().startsWith(targetDateStr)) {
                        bestMatch = item;
                        if (hora != null && item.getDt_txt().contains(hora.substring(0, 2))) {
                            break;
                        }
                    }
                }

                if (bestMatch != null) {
                    return new CondicionesMeteorologicas(
                            (int) Math.round(bestMatch.getMain().getTemp()),
                            bestMatch.getWeather().get(0).getDescription(),
                            bestMatch.getMain().getHumidity(),
                            bestMatch.getWeather().get(0).getIcon());
                }
            }
        } catch (Exception e) {
            System.err.println("Error al obtener pronostico: " + e.getMessage());
        }
        return ejecutarPeticionClima(url.replace("/forecast", "/weather")); // Fallback a clima actual
    }

    // Mapeo de respuesta JSON de la API a objetos Java (POJOs)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WeatherResponse {
        private Main main;
        private List<Weather> weather;

        public Main getMain() {
            return main;
        }

        public void setMain(Main main) {
            this.main = main;
        }

        public List<Weather> getWeather() {
            return weather;
        }

        public void setWeather(List<Weather> weather) {
            this.weather = weather;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ForecastResponse {
        private List<ForecastItem> list;

        public List<ForecastItem> getList() {
            return list;
        }

        public void setList(List<ForecastItem> list) {
            this.list = list;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ForecastItem {
        private Main main;
        private List<Weather> weather;
        private String dt_txt;

        public Main getMain() {
            return main;
        }

        public void setMain(Main main) {
            this.main = main;
        }

        public List<Weather> getWeather() {
            return weather;
        }

        public void setWeather(List<Weather> weather) {
            this.weather = weather;
        }

        public String getDt_txt() {
            return dt_txt;
        }

        public void setDt_txt(String dt_txt) {
            this.dt_txt = dt_txt;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Main {
        private Double temp;
        private Integer humidity;

        public Double getTemp() {
            return temp;
        }

        public void setTemp(Double temp) {
            this.temp = temp;
        }

        public Integer getHumidity() {
            return humidity;
        }

        public void setHumidity(Integer humidity) {
            this.humidity = humidity;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Weather {
        private String description;
        private String icon;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
}
