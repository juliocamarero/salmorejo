#include <SPI.h>
#include <Ethernet.h>
#include <string.h>

byte mac[] = {
  0x00, 0xAA, 0xBB, 0xCC, 0xDE, 0x02
};

IPAddress server(192,168,50,231);
int port = 9090;

unsigned long lastConnectionTime = 0;
const unsigned long postingInterval = 10L * 1000L;

int redLed = 9;
int greenLed = 8;

EthernetClient client;

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
void httpRequest() {
  // close any connection before send a new request.
  // This will free the socket on the WiFi shield
  client.stop();

  // if there's a successful connection:
  if (client.connect(server, port)) {
    Serial.println("connecting...");
    // send the HTTP GET request:
    client.println("GET / HTTP/1.0");
    client.println();

    // note the time that the connection was made:
    lastConnectionTime = millis();
  } else {
    // if you couldn't make a connection:
    Serial.println("connection failed");
  }
}

void setup() {
  Serial.begin(9600);

  pinMode(redLed, OUTPUT);
  pinMode(greenLed, OUTPUT);

  digitalWrite(redLed, LOW);
  digitalWrite(greenLed, HIGH);    

  // start the Ethernet connection:
  EthernetConnect();
}

String body = "";

int flag = 0;

void loop() {

  if (flag == 0) {
    Serial.println("Making request");
    delay(1000);
    httpRequest();
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


