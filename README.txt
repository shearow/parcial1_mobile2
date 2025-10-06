
    Parcial N°1 Programación de App Móviles II 

App:
Esta aplicación se basa en extraer los servicios de una API de clima llamada Weatherbit, y dentro de la misma ofrecerle al usuario los 
diferentes climas de las distintas ciudades que el mismo desee observar, con diversa información más detallada en diferentes secciones.
Además puede guardar en una lista de favoritos las ciudades que más le interesen así como también eliminarlas. Por otra parte, esta app 
posee una pantalla desde la cuál los usuarios pueden personalizar algunos aspectos generales tales como el idioma, el tamaño de la letra 
entre otras cosas.

Funcionamiento:
La aplicación utiliza el deadpoint de https://api.weatherbit.io/v2.0/ con el método GET y la palabra current, más el añadido del nombre 
de la ciudad y el API_KEY que se encuentra en un archivo object de carácter privado. Este endpoint básicamente lo que hace es mostrarnos 
el clima actual de la ciudad que el usuario ingrese. Los datos que se extraen de este endpoint son nombre de la ciudad, código del país, 
temperatura actual, y un string de infromación del clima (nublado, lluvioso, soleado, etc) más un ícono ilustrativo del mismo basado en 
el estado en que se encuentre el clima. 
Por otro lado, utilizamos el endpoint forecast/daily, sumado obviamente a la URL previamente mencionada. Este método nos trae información 
más detallada, y además nos proporciona el clima de hasta los próximos 7 días. Este endpoint nos proporciona la siguiente información a 
cerca del clima: fecha del pronóstico, temperatura máxima y mínima, probabilidad de precipitación, precipitación acumulada, humedad 
relativa, velocidad del viento, y un objeto el cuál posee la descripción del clima y una imagen ilustrativa del mismo.
Cabe aclarar que esta es la información que nosotros utilizamos dentro de nuestra aplicación; la API proporciona más datos, pero en este 
caso nosotros no los utilizamos.

Producción del trabajo:
Fue realizado por partes, iniciando por los layouts, luego por la parte de la conexión con la API y las estrcuturas de datos, posteriormente 
continuamos con el manejo de errores y por último la parte de la configuración. Fue un trabajo el cuál se trabajó gran parte vía WhatsApp 
con el uso del repositorio en GitHub.

Ejecución de la App:
Simplemente es necesario abrir el Android Studio y ejecutar el programa, ya que las API_KEY y la configuración del mismo está toda automatizada 
e incluida dentro del propio proyecto.
Ni bién la App se abra, aparecerá una pantalla con el título de la aplicación, una barra de búsqueda con un botón para buscar, y una caja vacía 
en la cuál se añadirán las ciudades que posteriormente a su búsqueda, se marquen como favoritas desde un corazón que se encuentra en la parte 
superior derecha. Por otro lado, se encuentra el logo de configuración el cuál te lleva a otra pantalla donde podés seleccionar algunas 
configuracuiones generales.

Integrantes:
Tomás Curien, Nicolás García Bietti, Benjamín Manzi.