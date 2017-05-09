#include <SPI.h>
#include <Ethernet.h>
#include <string.h>

byte mac[] = {
  0x00, 0xAA, 0xBB, 0xCC, 0xDE, 0x02
};

IPAddress server(192,168,50,231);
int port = 9090;

EthernetClient client;

int redLed = 9;
int greenLed = 8;
int yellowLed = 2;

String body = "";

int flag = 0;

void EthernetConnect() {
  if (Ethernet.begin(mac) == 0) {
    Serial.println("Failed to configure Ethernet using DHCP");
    // no point in carrying on, so do nothing forevermore:
    for (;;)
      ;
  }
  printIPAddress();
}

void printIPAddress() {
  Serial.print("My IP address: ");
  for (byte thisByte = 0; thisByte < 4; thisByte++) {
    // print the value of each byte of the IP address:
    Serial.print(Ethernet.localIP()[thisByte], DEC);
    Serial.print(".");
  }

  Serial.println();
}

// this method makes a HTTP connection to the server:
bool httpRequest() {
  // close any connection before send a new request.
  // This will free the socket on the WiFi shield
  client.stop();

  // if there's a successful connection:
  if (client.connect(server, port)) {
    Serial.println("connecting...");

    // send the HTTP GET request:
    client.println("GET / HTTP/1.0");
    client.println();

    return true;
  } else {
    // if you couldn't make a connection:
    Serial.println("connection failed");

    return false;
  }
}

void setup() {
  Serial.begin(9600);

  pinMode(redLed, OUTPUT);
  pinMode(greenLed, OUTPUT);
  pinMode(yellowLed, OUTPUT);

  digitalWrite(redLed, LOW);
  digitalWrite(greenLed, HIGH);    

  // start the Ethernet connection:
  EthernetConnect();
}

void loop() {

  if (flag == 0) {
    Serial.println("Making request");
    delay(1000);
    if (!httpRequest()) {
      digitalWrite(redLed, LOW);
      digitalWrite(greenLed, LOW);
      digitalWrite(yellowLed, HIGH);
    } else {
     digitalWrite(yellowLed, LOW); 
    }
  }

  if (client.connected()) {
    if (client.available()) {
      body += (char) client.read();
    }

    flag = 1;
  }
  else {
    flag = 0;

    if (body != "") {
      if(body.endsWith("busy")) {
        digitalWrite(redLed, HIGH);
        digitalWrite(greenLed, LOW);
      }
      else if(body.endsWith("idle")) {
        digitalWrite(redLed, LOW);
        digitalWrite(greenLed, HIGH);
      }
      Serial.println(body);
      body = "";
    }
  
  }

  
}


