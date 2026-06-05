# Sistema de Gestión para Profesionales de Estética

## Presentación general

Esta aplicación es un sistema de gestión pensado para profesionales de estética que necesitan organizar su trabajo diario de manera clara, segura y profesional.

Permite administrar desde un solo lugar la información más importante del negocio: profesionales, accesos, pacientes, fichas clínicas, servicios, turnos, sesiones, fotos de evolución, pagos, deudas y bloqueos de agenda.

La idea principal es reemplazar la información dispersa en cuadernos, planillas, mensajes de WhatsApp, notas sueltas o recordatorios manuales por una herramienta centralizada, ordenada y preparada para crecer.

---

## ¿Para quién sirve?

La app está pensada para profesionales independientes, gabinetes o centros de estética que atienden pacientes y necesitan llevar un seguimiento organizado de su actividad.

Puede ser útil para:

- Cosmetólogas.
- Cosmiatras.
- Esteticistas.
- Profesionales de tratamientos faciales.
- Profesionales de tratamientos corporales.
- Gabinetes de estética.
- Centros pequeños o medianos con varias profesionales.
- Negocios que quieren ordenar pacientes, turnos, pagos e historias clínicas.

También está preparada para un escenario donde varias profesionales usan el mismo sistema, pero cada una ve únicamente su propia información. La creación de cuentas está centralizada en una administradora del sistema, evitando registros abiertos o accesos no controlados.

---

## ¿Qué problema resuelve?

En muchos gabinetes o centros de estética, la información suele estar repartida en distintos lugares:

- Los turnos en una agenda física o en Google Calendar.
- Los pagos en una planilla o en mensajes.
- Las fotos en la galería del celular.
- Las fichas clínicas en papel.
- Los precios en listas separadas.
- Las observaciones de cada sesión en notas o chats.

Esto puede generar problemas como:

- Pérdida de información importante.
- Dificultad para saber qué tratamiento se hizo en la última sesión.
- Confusión entre precios viejos y precios actuales.
- Falta de control sobre pagos parciales o deudas.
- Riesgo de agendar turnos en horarios no disponibles.
- Fotos de evolución desordenadas.
- Dificultad para encontrar rápidamente la ficha de una paciente.
- Mezcla de información entre profesionales.
- Falta de control sobre quién puede crear nuevas cuentas.
- Uso de contraseñas iniciales sin obligación de cambiarlas.

La app resuelve estos problemas centralizando todo el flujo de trabajo en un sistema único.

---

## Cómo funciona la app en la práctica

El funcionamiento está pensado para acompañar el trabajo real de una profesional de estética.

Un flujo típico sería:

1. El sistema crea una cuenta inicial de administración.
2. La administradora inicia sesión, cambia su contraseña inicial y queda habilitada para usar el sistema.
3. Desde el panel de administración crea las cuentas de las profesionales.
4. Cada profesional ingresa por primera vez con la contraseña inicial asignada.
5. Antes de usar cualquier módulo, el sistema le exige cambiar esa contraseña por una propia.
6. Una vez cambiado el password, la profesional carga o consulta sus pacientes.
7. Define los servicios que ofrece y sus precios.
8. Crea un turno para una paciente, seleccionando uno o más servicios.
9. El sistema calcula automáticamente el monto total del turno.
10. Cuando el turno se realiza, registra la sesión clínica.
11. Puede agregar fotos de evolución asociadas a esa sesión.
12. Registra los pagos recibidos, ya sean completos, parciales, señas o trueques.
13. Si tiene horarios no disponibles, bloquea su agenda para evitar turnos en esos rangos.
14. Toda la información queda asociada a esa profesional y no se mezcla con la de otras.

---

## Módulos principales

### 1. Administración, roles y alta de profesionales

La app cuenta con un rol ADMIN para gestionar las cuentas de profesionales.

La cuenta inicial que crea el sistema es:

- Email: admin@estetica.local.
- Rol: ADMIN.
- Contraseña inicial: definida por el backend.
- Debe cambiar su contraseña en el primer ingreso.

Desde el panel de administración se puede:

- Crear profesionales.
- Listar profesionales.
- Editar los datos básicos de una profesional.
- Resetear la contraseña de una profesional.
- Dar de baja profesionales.

Cuando una administradora crea una profesional, esa cuenta nace con:

- Rol PROFESIONAL.
- Contraseña inicial hasheada en la base de datos.
- Obligación de cambiar la contraseña en el primer ingreso.

Si una profesional olvida su contraseña o necesita recuperar el acceso, la administradora puede resetearla desde el panel. La nueva contraseña se guarda hasheada y la profesional queda marcada nuevamente con cambio obligatorio, por lo que deberá elegir una contraseña propia en su próximo ingreso.

No existe registro público abierto. Esto significa que una persona no puede darse de alta sola desde el frontend; las cuentas profesionales se crean desde administración.

---

### 2. Profesionales

Cada profesional tiene su propio perfil dentro del sistema.

Desde ahí se organiza toda su información:

- Sus pacientes.
- Sus servicios.
- Sus turnos.
- Sus historias clínicas.
- Sus sesiones.
- Sus pagos.
- Sus bloqueos de agenda.

Esto permite que el sistema pueda ser usado por una sola profesional o por varias dentro de un mismo centro, manteniendo siempre los datos separados.

En términos simples: cada profesional trabaja en su propio espacio privado.

Cada profesional puede consultar y actualizar su propio perfil, pero no puede acceder a las operaciones administrativas si no tiene rol ADMIN.

---

### 3. Pacientes

La app permite registrar y administrar pacientes con sus datos principales.

Entre la información que se puede guardar se encuentra:

- Nombre y apellido.
- DNI o CUIT.
- Fecha de nacimiento.
- Teléfono.
- Email.
- Profesión.
- Domicilio.
- Obra social.
- Número de obra social.
- Contacto de emergencia.
- Entidades de traslado.

Los datos básicos como nombre, apellido, DNI y teléfono ayudan a identificar y contactar a la paciente de forma rápida.

Además, la app permite archivar pacientes mediante una baja lógica. Esto significa que si una paciente deja de atenderse, no hace falta borrar su historial. Se puede marcar como inactiva y conservar toda la información registrada.

Esto es importante porque en estética el historial puede seguir siendo útil aunque la paciente no vuelva durante un tiempo.

---

### 4. Servicios

Cada profesional puede crear su propia lista de servicios.

Por ejemplo:

- Limpieza facial.
- Peeling.
- Radiofrecuencia.
- Drenaje linfático.
- Tratamientos corporales.
- Tratamientos faciales.
- Masajes.
- Depilación.
- Otros servicios personalizados.

Para cada servicio se puede indicar:

- Nombre.
- Descripción.
- Precio.
- Estado activo o inactivo.

Si un servicio deja de ofrecerse, no es necesario borrarlo. Se puede desactivar para que ya no figure como disponible, pero se mantiene el historial de turnos donde fue utilizado.

Esto evita perder información pasada y permite mantener una lista actualizada de servicios ofrecidos.

---

### 5. Turnos

La app permite crear turnos asociados a una profesional, una paciente y uno o más servicios.

Cada turno incluye:

- Fecha y hora.
- Paciente.
- Servicios incluidos.
- Estado del turno.
- Monto total.
- Observaciones.

Los estados posibles del turno son:

- Pendiente.
- Confirmado.
- Realizado.
- Cancelado.

Esto permite saber rápidamente en qué situación está cada atención.

Por ejemplo:

- Un turno recién creado puede quedar como pendiente.
- Si la paciente confirma, pasa a confirmado.
- Cuando se atiende, pasa a realizado.
- Si no se concreta, puede marcarse como cancelado.

La app también controla que no se creen turnos en fechas pasadas ni en horarios bloqueados por la profesional.

---

### 6. Varios servicios en un mismo turno

Un turno puede incluir uno o varios servicios.

Por ejemplo, una paciente podría reservar en una misma visita:

- Limpieza facial.
- Peeling.
- Máscara hidratante.

El sistema suma automáticamente los precios de esos servicios y calcula el monto total del turno.

Además, guarda el precio que tenía cada servicio en el momento de crear el turno. Esto es importante porque si más adelante la profesional aumenta sus precios, los turnos anteriores siguen conservando el precio original.

Ejemplo:

- Hoy un servicio cuesta $50.000.
- Se crea un turno con ese servicio.
- Más adelante el servicio aumenta a $60.000.
- El turno viejo sigue mostrando $50.000, porque ese era el precio al momento de reservarlo.

Esto evita confusiones y mantiene un historial económico correcto.

---

### 7. Historia clínica facial

La app permite crear una historia clínica facial para cada paciente.

En esta ficha se puede registrar información como:

- Antecedentes patológicos.
- Antecedentes tóxicos.
- Alergias.
- Cirugías previas.
- Antecedentes ginecológicos.
- Medicación habitual.
- Hábitos de exposición solar.
- Uso de protector solar.
- Higiene facial.
- Tratamientos previos.
- Examen facial.
- Fototipo Fitzpatrick.
- Grado Glogau.
- Diagnóstico y tratamiento sugerido.
- Observaciones posteriores.

La ficha facial puede completarse de manera progresiva. Esto significa que la profesional no está obligada a cargar toda la información desde el primer día. Puede empezar con lo disponible y actualizar la ficha a medida que avanza el tratamiento.

Esto refleja mejor la realidad del trabajo profesional, donde muchas veces la información clínica se completa con el tiempo.

---

### 8. Historia clínica corporal

Además de la ficha facial, el sistema permite registrar una historia clínica corporal.

Esta ficha puede incluir:

- Antecedentes de salud.
- Alergias.
- Cirugías previas.
- Embarazo o lactancia.
- Hábitos alimenticios.
- Consumo de agua.
- Actividad física o sedentarismo.
- Ortostatismo prolongado.
- Uso de medias de compresión.
- Tratamientos corporales previos.
- Presencia de arañas vasculares, telangiectasias, várices, celulitis, flacidez o estrías.
- Adiposidad localizada.
- Peso actual.
- Peso habitual.
- IMC.
- Perímetro de cintura.
- Diagnóstico y tratamiento.
- Observaciones posteriores.

Esto ayuda a realizar un seguimiento más completo de tratamientos corporales y a conservar información relevante para futuras sesiones.

---

### 9. Sesiones clínicas

Cuando un turno ya fue realizado, la profesional puede registrar la sesión clínica correspondiente.

En cada sesión se guarda:

- Número de sesión.
- Tratamiento realizado.
- Respuesta y tolerancia de la paciente.
- Observaciones.

El número de sesión se calcula automáticamente, lo que permite llevar un orden dentro del tratamiento de cada paciente.

Esto es útil para responder preguntas como:

- ¿Qué se hizo en la última sesión?
- ¿Cómo respondió la paciente?
- ¿Hubo alguna reacción?
- ¿Qué observaciones se dejaron para la próxima visita?

La sesión clínica transforma cada turno realizado en una parte del historial de tratamiento.

---

### 10. Fotos de evolución

La app permite vincular fotos de una paciente a una sesión clínica.

Esto sirve para hacer seguimiento visual de los resultados.

Algunos usos posibles:

- Fotos antes y después.
- Seguimiento de manchas.
- Evolución de tratamientos faciales.
- Evolución de tratamientos corporales.
- Comparación entre sesiones.
- Registro visual del progreso.

En esta primera versión, el sistema guarda la ruta de la imagen. Más adelante puede integrarse con servicios de almacenamiento en la nube.

El beneficio principal es que las fotos quedan relacionadas con la paciente y con la sesión correspondiente, en vez de quedar perdidas en la galería del celular.

---

### 11. Pagos

La app permite registrar pagos asociados a los turnos.

Los métodos de pago contemplados son:

- Efectivo.
- Transferencia.
- Mercado Pago.
- Trueque.

También se puede indicar si un pago corresponde a una seña.

Cada pago queda asociado a un turno, lo que permite saber cuánto se pagó, cuándo se pagó y con qué método.

---

### 12. Pagos parciales, combinados y deuda

Un turno no necesariamente se paga de una sola vez. La app permite registrar varios pagos para un mismo turno.

Por ejemplo:

- Una parte en efectivo.
- Otra parte por transferencia.
- Una seña anticipada.
- El saldo restante el día del turno.

De esta forma, un pago combinado no necesita un tipo especial. Simplemente se registra cada parte por separado.

El sistema calcula automáticamente:

- Monto total del turno.
- Monto pagado.
- Deuda pendiente.
- Si todavía queda deuda o no.

También evita registrar pagos que superen el monto total del turno.

Esto ayuda a tener un control claro de la parte económica y reduce errores administrativos.

---

### 13. Trueques

La app contempla el trueque como forma de pago.

Si una profesional acepta un intercambio, puede registrarlo y dejar detallado en qué consiste.

Por ejemplo:

- Intercambio por productos.
- Intercambio por sesión de fotos.
- Intercambio por otro servicio profesional.

Esto permite que el acuerdo quede documentado dentro del sistema y no dependa solamente de la memoria o de mensajes externos.

---

### 14. Bloqueos de agenda

La profesional puede bloquear rangos de fecha y hora en los que no está disponible.

Por ejemplo:

- No atiende de 14:00 a 16:00.
- Tiene vacaciones.
- Tiene una capacitación.
- Tiene un compromiso personal.
- El consultorio está cerrado.

Cuando existe un bloqueo, la app impide crear turnos dentro de ese rango.

También evita crear un bloqueo si ya existen turnos vigentes en ese horario. En ese caso, el sistema avisa que hay turnos agendados para que la profesional pueda moverlos o cancelarlos antes.

Esto ayuda a evitar superposiciones y errores en la agenda.

---

## Cómo se integran las funciones entre sí

La app no maneja cada módulo de forma aislada. Las funciones están conectadas para acompañar el flujo real de atención.

El recorrido completo sería:

1. La administradora crea la cuenta de una profesional.
2. La profesional inicia sesión y cambia su contraseña inicial.
3. La profesional tiene su perfil y sus datos privados.
4. Carga una paciente.
5. Completa sus historias clínicas facial y/o corporal.
6. Crea sus servicios con precios propios.
7. Agenda un turno para esa paciente con uno o más servicios.
8. El sistema calcula el total y guarda el precio de cada servicio en ese momento.
9. Si el turno se realiza, se registra una sesión clínica.
10. En esa sesión se describe qué tratamiento se hizo y cómo respondió la paciente.
11. Se pueden asociar fotos de evolución a la sesión.
12. Se registran pagos, señas, trueques o pagos parciales.
13. El sistema calcula automáticamente si queda deuda.
14. Si la profesional no está disponible en ciertos horarios, bloquea la agenda para evitar errores.

Esta integración permite que toda la información esté relacionada y sea fácil de consultar.

---

## Seguridad, roles y privacidad de los datos

La app está diseñada para que cada profesional vea únicamente su propia información.

Esto significa que:

- Una profesional no puede ver pacientes de otra profesional.
- Una profesional no puede modificar servicios de otra profesional.
- Una profesional no puede acceder a turnos ajenos.
- Las fichas clínicas quedan protegidas dentro del espacio de cada profesional.
- Los pagos y fotos también pertenecen a la profesional correspondiente.
- Las operaciones administrativas solo pueden usarse con rol ADMIN.
- Las profesionales con rol PROFESIONAL no pueden crear, listar, editar, resetear contraseñas ni dar de baja otras profesionales.

Este modelo es importante para centros donde trabajan varias profesionales y para cualquier negocio que quiera cuidar la privacidad de sus pacientes.

En palabras simples: cada profesional tiene su propio espacio de trabajo y sus datos no se mezclan con los de las demás.

La autenticación funciona con JWT. Al iniciar sesión, el backend devuelve un token que el frontend debe enviar en cada petición protegida usando el header Authorization con formato Bearer.

El login también devuelve:

- debeCambiarPassword: indica si la usuaria debe cambiar la contraseña inicial.
- rol: indica si la cuenta es ADMIN o PROFESIONAL.

Si una cuenta tiene debeCambiarPassword en true, el backend bloquea el uso de los endpoints protegidos hasta que cambie su contraseña. Solo quedan permitidos el login y el endpoint de cambio de contraseña.

La configuración CORS permite peticiones autenticadas solamente desde http://localhost:5173, que es el origen previsto para el frontend local.

---

## Beneficios para la profesional

La aplicación ayuda a la profesional a trabajar con más orden y menos carga administrativa.

Beneficios principales:

- Tener toda la información en un solo lugar.
- Encontrar rápidamente datos de pacientes.
- Consultar fichas clínicas cuando sea necesario.
- Saber qué tratamiento se realizó en cada sesión.
- Comparar fotos de evolución.
- Controlar pagos y deudas.
- Evitar errores de agenda.
- Mantener precios históricos correctos.
- Archivar pacientes sin borrar información.
- Reducir la dependencia de papel, planillas y chats.

---

## Beneficios para el negocio

Además de ayudar en el día a día, el sistema mejora la organización general del negocio.

Aporta:

- Imagen más profesional.
- Mejor seguimiento de pacientes.
- Menos errores administrativos.
- Mayor control económico.
- Mayor seguridad sobre la información.
- Mejor trazabilidad de tratamientos.
- Base ordenada para crecer en el futuro.

Para un centro de estética, esto permite pasar de una gestión informal a una gestión más profesional y escalable.

---

## Ejemplo simple de uso

Una profesional atiende a una paciente nueva.

Primero la carga en el sistema con sus datos básicos. Luego completa su historia clínica facial, anotando antecedentes, alergias, hábitos y diagnóstico inicial.

Después crea un servicio llamado “Limpieza facial profunda” con su precio. Agenda un turno para esa paciente y selecciona ese servicio.

Cuando llega el día del turno, marca el turno como realizado y registra la sesión clínica: qué tratamiento hizo, cómo respondió la paciente y qué observaciones quedaron.

También puede agregar fotos de evolución y registrar si la paciente pagó completo, dejó una seña o quedó con una deuda pendiente.

Si la profesional sabe que el viernes de 14:00 a 16:00 no va a atender, bloquea ese horario para que no se creen turnos ahí.

Todo queda guardado y conectado: paciente, ficha clínica, turno, sesión, fotos y pagos.

---

## Qué incluye esta versión

Esta versión permite gestionar:

- Administración de profesionales con rol ADMIN.
- Creación, listado, edición, reseteo de contraseña y baja de profesionales desde administración.
- Profesionales con acceso seguro mediante JWT.
- Roles ADMIN y PROFESIONAL.
- Cambio obligatorio de contraseña inicial.
- Login que informa token, rol y estado de cambio de password.
- Pacientes.
- Servicios.
- Turnos.
- Historias clínicas faciales.
- Historias clínicas corporales.
- Sesiones clínicas.
- Fotos de pacientes asociadas a sesiones.
- Pagos, señas, pagos parciales, deudas y trueques.
- Bloqueos de agenda.
- Separación segura de información entre profesionales.
- CORS configurado para el frontend local en http://localhost:5173.

---

## Qué no incluye esta primera versión

Esta primera versión está enfocada en la gestión interna de la profesional.

Por ahora, no incluye:

- Registro público de profesionales.
- Creación de cuentas desde una pantalla pública de signup.
- App para pacientes.
- Agenda online para que las pacientes saquen turno solas.
- Recordatorios automáticos por WhatsApp o email.
- Integración con Google Calendar.
- Facturación electrónica.
- Reportes avanzados o estadísticas comerciales.
- Cobros automáticos integrados.
- Almacenamiento real de imágenes en la nube.

Estas funciones pueden agregarse en futuras etapas según las necesidades del negocio.

---

## Resumen final para clientes

Esta app ayuda a profesionales de estética a ordenar y profesionalizar su trabajo diario.

Permite administrar profesionales, pacientes, fichas clínicas, servicios, turnos, sesiones, fotos, pagos y agenda desde un solo lugar.

Su principal valor es que conecta toda la información importante del proceso de atención: desde la creación segura de la cuenta profesional, hasta que una paciente se registra, realiza un tratamiento, se documenta la sesión, se guardan fotos de evolución y se controla el pago.

En pocas palabras: es una herramienta para trabajar con más orden, más control y una imagen más profesional frente a las pacientes.
