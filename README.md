# RTAVPAWS
Real-Time Vehicle Parking Application with Wireless Sensor

An Android application as part of the final year project. This projects aims to solve the problem of parking. As everyone experienced before, finding a parking space can be dramatically problematic, specially during peak hours. The main idea of this project is to provide the driver an interface that allows pre booking of a space. This would be extensively time efficient. The driver can search for a parking space, which for this prototype was a driveway that is owned by someone. This driveway can be advertised and availability set, which is then shown to other drivers interested in parking.

A wireless sensor can optionally be opted by the driveway owner which provides extra security to both drivers and owners. In this case, it tracks the attendance and provides real-time space availability for the driver. Advantage of opting, includes the ability for the driver to know if the space is available and avoiding the last minute inconvenient of reaching the place and see the driveway blocked.

The application was developed in Android Studio using Java. Libraries were used for design and other features. The application also supports QR Code to pair the sensor with the account. Database used is Firebase(NoSQL) for real-time purposes.

API's were also used to validate postcodes and location ranges:

• https://postcodes.io
• https://getaddress.io

The wireless sensor was purely built using Raspberry Pi and the distance sensor(HC-SR04). The distance sensor is an ultra-sonic sensor to measure waves between the object and the sensor.

This prototype has the ability to:

• Authentication
• Booking/Cancellation
• Add slot
• Payment
• Retrieve data from sensor in real-time
• Disallow booking if sensor blocked
• Checking availability
• Search for parking
