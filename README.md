# Wallets Service
In Playtomic, we have a service to manage our wallets. Our players can recharge their wallets using a credit card and spend that money on the platform  (bookings, racket rentals, ...)

This service has the following operations:
- You can query your balance.
- You can recharge your wallet. In this case, we charge the amount using a third-party payments platform (stripe, paypal, redsys).
- You can spend your balance on purchases in Playtomic. 
- You can return these purchases, and your money is refunded.
- You can check your history of transactions.

This exercise consists of building a proof of concept of this wallet service.
You have to code endpoints for these operations:
1. Get a wallet using its identifier.
1. Recharge money in that wallet using a credit card number. It has to charge that amount internally using a third-party platform.
1. Subtract an amount from a wallet (that is, make a charge in that wallet).

The basic structure of a wallet is its identifier and its current balance. If you think you need extra fields, add them. We will discuss it in the interview. 

So you can focus on these problems, you have here a maven project with a Spring Boot application. It already contains
the basic dependencies and an H2 database. There are development and test profiles.

You can also find an implementation of the service that would call to the real payments platform (StripePaymentService).
You don't have to code that; assume that this service is making a remote request to a third-party system. 
This dummy implementation returns errors under certain conditions.

Consider that this service would be working in a microservices environment, and you should care about concurrency.

You can spend as much time as you need but we think that 3-4 hours is enough to show [the requirements of this job.](OFFER.md)
You don't have to document your code, but you can write down anything you want to explain or anything you have skipped.
You don't need to write tests for everything, but we would like to see different types of tests.


# Servicio de bono monedero
En Playtomic tenemos un servicio de bono monedero. Los jugadores pueden recargar ese bono con su tarjeta de crédito y gastar ese dinero en la plataforma (reservas, alquiler de raquetas, ...).

Ese servicio tiene las siguientes operaciones:
- Puedes consultar el saldo.
- Puedes cargar dinero. En este caso haciendo un cobro una pasarela pasarela de pagos de terceros (stripe, paypal, redsys).
- Puedes gastar el saldo en compras en nuestra plataforma.
- Puedes devolver esas compras y recuperar el saldo.
- Puedes consultar el histórico de gastos y devoluciones.

Este ejercicio consiste en construir una prueba de concepto simplificada de este servicio de bono monedero. Sólo tienes que implementar las siguientes operaciones:

El ejercicio consiste en que programes endpoints para:
1. Consultar un bono monedero por su identificador.
1. Recargar dinero en ese bono a través de un servicio de pago de terceros.
1. Descontar saldo del monedero.

La estructura básica que te proponemos para monedero es su identificador y su saldo actual. Si consideras que necesitas más campos,
añádelos sin problemas. Lo discutiremos en la entrevista.

Para que puedas ir al grano, te damos un proyecto maven con una aplicación Spring Boot 2.x, con las dependencias básicas y una
base de datos H2. Tienes perfiles de develop y test.

Hemos incluído una clase que simularía la llamada a la pasarela de pago real (StripePaymentService).
Esa parte no tienes que programarla, asume que el servicio hace la llamada remota dada una cantidad de dinero.
Está pensado para que devuelva error bajo ciertas condiciones.

Ten en cuenta que es un servicio que conviviría en un entorno de microservicios y alta concurrencia.

Le puedes dedicar el tiempo que quieras, pero hemos estimado que 3-4 horas es suficiente para demostrar [los requisitos del puesto.](OFFER.md)
No hace falta que lo documentes pero puedes anotar todo lo que quieras como explicación o justificación de lo que hagas o dejes sin hacer.
Tampoco necesitas hacer testing de todo, pero sí nos gustaría ver varios tipos de test.

Things to improve:

- authorize the endpoints (e.g. use a JWT token that needs to be validated for every request). 
  This validation should also check that wallet from the request corresponds to that from the JWT, for instance.
  Currently, user A can find / update the wallet of user B just by guessing his/ her wallet id.
- encrypt the credit card number (don’t send the credit card number in plain text in the request body, encrypt it on the client side)
- in case the request to the Stripe service fails for reasons other than BAD REQUEST (other status codes can also be included here), have a retry mechanism for the call. 
  This should be discussed though, as retrying a payment call might not be allowed. The main idea is that given the microservices architecture, we should take into account the possible failures
- there should also be an audit table where all the changes made to the wallets are recorded
- Add unit tests for all the services/ exception handlers: cover both the positive & negative scenarios
- More ITs for the concurrent calls (e.g. recharge & find by id, charge & find by id) - to check that the values are consistent
- In case of BAD_REQUEST responses, include the reason of failure in the error response (get it from the ConstraintViolations)
- Have business & error metrics (e.g. total of top-ups, payments, Stripe call failures) from the application
- Check if the logs can be improved

If IntelliJ does not recognize Lombok, please go to Preferences, search for Plugins. Type "Lombok" in the plugin search box.
The "Lombok Plugin" should show up. :)