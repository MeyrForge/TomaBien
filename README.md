# TomaBien

Aplicación de seguimiento de toma de medicamentos con sistema de alarmas/notificaciones.

## Descripción

TomaBien es una aplicación para Android diseñada para ayudar a los usuarios a gestionar sus medicamentos de manera efectiva. Ofrece una interfaz simple e intuitiva para llevar un seguimiento de los horarios de medicación, configurar recordatorios y asegurarse de que los usuarios nunca se salten una dosis. Esta aplicación es ideal para personas que toman múltiples medicamentos o que necesitan un sistema confiable para mantenerse al día con su régimen de salud.

## Funcionalidades Clave

*   **Seguimiento de Medicación:** Agrega, edita y elimina medicamentos fácilmente.
*   **Alarmas Personalizables:** Configura recordatorios flexibles para cada medicamento, asegurando una toma a tiempo.
*   **Gestión de Dosis:** Especifica la información de dosis para cada medicamento.
*   **Interfaz Intuitiva:** Diseño fácil de usar para una navegación y gestión sencilla.

## Capturas de Pantalla

**Mis Medicaciones**
<div align="center">
    <img src="screenshots/mis-medicaciones-vacio.png" alt="Mis medicaciones pantalla vacia" style="width: 30%; margin: 10px;">
    <img src="screenshots/mis-medicaciones-lleno.png" alt="Mis medicaciones pantalla llena" style="width: 30%; margin: 10px;">
</div>

**Agregar Medicación**
<div align="center">
    <img src="screenshots/agregar-medicacion-vacio.png" alt="Agregar medicacion" style="width: 30%; margin: 10px;">
    <img src="screenshots/agregar-medicacion-lleno.png" alt="Agregar medicacion" style="width: 30%; margin: 10px;">
</div>

**Editar Medicación**
<div align="center">
    <img src="screenshots/editar-medicacion.png" alt="Editar medicacion" style="width: 30%; margin: 10px;">
</div>

**Alarmas**
<div align="center">
    <img src="screenshots/alarmas-vacio.png" alt="Alarmas pantalla vacia" style="width: 30%; margin: 10px;">
    <img src="screenshots/agregar-alarma.png" alt="Agregar alarma" style="width: 30%; margin: 10px;"> 
   <img src="screenshots/alarmas-lleno.png" alt="Alarmas pantalla llena" style="width: 30%; margin: 10px;">
</div>

**Seguimiento**
<div align="center">
    <img src="screenshots/seguimiento-tildado.png" alt="Seguimiento tildado" style="width: 30%; margin: 10px;">
    <img src="screenshots/seguimiento-sintildar.png" alt="Seguimiento sin tildar" style="width: 30%; margin: 10px;">
</div>

**Resumen de toma**
<div align="center">
    <img src="screenshots/resumen.png" alt="Resumen de toma" style="width: 30%; margin: 10px;">
</div>



## Uso
1. **Agregar una medicación:**
    *   Navegar a la pantalla de 'Mis medicaciones'.
    *   Pulsar botón flotante de 'Agregar medicación'.
    *   Ingresa el nombre del medicamento, la dosis y si es opcional o no.
2. **Gestión de alarmas:**
    *   Seleccionar el ícono de alarma del medicamento requerido en la pantalla 'Mis medicaciones'.
    *   Agregar alarma pulsando el botón flotante de 'Agregar alarma' o eliminarla pulsando el ícono de basura según lo desee.
    *   Una alarma puede ser creada o eliminada pero no editada.
3. **Gestión de toma:**
    *   Navegar a la pantalla 'Seguimiento' donde se mostrarán las alarmas creadas anteriormente y el medicamento correspondiente a cada una de ellas.
    *   Tildar las casillas de los medicamentos ingeridos ese día.
4. **Resumen de toma:**
    *   Navegar a la pantalla 'Resumen de toma'.
    *   Se mostrarán, agrupados por fecha, los medicamentos que han sido tomados según lo tildado en la pantalla anterior.

### Tecnologías usadas

![Android](https://img.shields.io/badge/Android-Green?style=for-the-badge&logo=android&logoColor=white)
![AndroidStudio](https://img.shields.io/badge/AndroidStudio-limegreen?style=for-the-badge&logo=androidstudio&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-purple?style=for-the-badge&logo=kotlin&logoColor=white)
![JetpackCompose](https://img.shields.io/badge/JetpackCompose-deepskyblue?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Room Database](https://img.shields.io/badge/RoomDatabase-F58220?style=for-the-badge&logo=android&logoColor=white)
![Dagger Hilt](https://img.shields.io/badge/Hilt-5091CD?style=for-the-badge&logo=maildotru&logoColor=white)
![Clean Architecture](https://img.shields.io/badge/Clean-Architecture-D6180B?style=for-the-badge&logo=&logoColor=white)
