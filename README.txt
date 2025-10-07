# Parcial N°1 - Programación de Aplicaciones Móviles II

## App
Esta aplicación se basa en consumir los servicios de una **API de clima llamada Weatherbit**, y dentro de la misma ofrecerle al usuario los diferentes climas de las ciudades que desee observar, con información detallada en distintas secciones.  

Además, permite **guardar y eliminar ciudades favoritas**.

---

## Funcionamiento
La aplicación consume los endpoints públicos de la API **Weatherbit** mediante el cliente **Retrofit**, utilizando **GsonConverter** para transformar las respuestas JSON en objetos Kotlin.  

Se utilizan los siguientes endpoints con el método **GET**:
- [`https://api.weatherbit.io/v2.0/current`](https://api.weatherbit.io/v2.0/current)
- [`https://api.weatherbit.io/v2.0/forecast/daily`](https://api.weatherbit.io/v2.0/forecast/daily)

La **API_KEY** y la **BASE_URL** se encuentran definidas en el archivo `config.kt`.

---

### 🔹 Endpoint `/current`
**¿Qué proporciona este endpoint?**  
Devuelve el **clima actual** de la ciudad ingresada por el usuario.  

**Parámetros utilizados:**
- `city`: nombre de la ciudad a buscar  
- `key`: API_KEY privada  

**Datos que se utilizan en la app:**
- `city_name` → nombre de la ciudad  
- `country_code` → código del país  
- `temp` → temperatura actual  
- `weather.description` → descripción del clima (nublado, lluvioso, soleado, etc.)  
- `weather.icon` → ícono ilustrativo basado en el estado del clima  

---

### 🔹 Endpoint `/forecast/daily`
**¿Qué proporciona este endpoint?**  
Devuelve el **pronóstico extendido de los próximos días** con información más detallada sobre el clima.  

**Parámetros utilizados:**
- `city`: nombre de la ciudad a buscar  
- `days`: cantidad de días de pronóstico a devolver  
- `key`: API_KEY privada  

**Datos que se utilizan en la app:**
- `city_name` → nombre de la ciudad  
- `country_code` → código del país  
- `data` → lista con la información del pronóstico de cada día, que incluye:  
  - `valid_date`: fecha del pronóstico  
  - `max_temp`: temperatura máxima  
  - `min_temp`: temperatura mínima  
  - `pop`: probabilidad de precipitación (%)  
  - `precip`: precipitación acumulada (mm)  
  - `rh`: humedad relativa (%)  
  - `wind_spd`: velocidad del viento (m/s)  
  - `weather.description`: descripción del clima  
  - `weather.icon`: ícono ilustrativo del clima  

> La API proporciona muchos más datos, pero en esta aplicación solo se utilizan los mencionados.

---

## Producción del trabajo
El desarrollo se realizó por etapas:
1. Creación de **layouts** y estructura visual.  
2. Implementación de la **conexión con la API** y las **estructuras de datos**.  
3. Manejo de **errores y validaciones**.

El trabajo fue coordinado principalmente a través de **WhatsApp** y con control de versiones mediante **GitHub**.

---

## Ejecución de la App
1. Abrir el proyecto en **Android Studio**.  
2. Sincronizar las dependencias de **Gradle**.  
3. Ejecutar la aplicación en un emulador o dispositivo físico.  

La **API_KEY** y la configuración están automatizadas dentro del proyecto.  

Al iniciar la app, se muestra una pantalla con el título de la aplicación, una barra de búsqueda, un botón de búsqueda y una lista vacía donde se añadirán las ciudades marcadas como favoritas (desde el ícono de corazón en la parte superior derecha).  

También se incluye un ícono que lleva a una pantalla con más información sobre la aplicación y sus desarrolladores.

---

## Integrantes
- Tomás Curien  
- Nicolás García Bietti  
- Benjamín Manzi
