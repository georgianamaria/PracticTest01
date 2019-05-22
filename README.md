# PracticTest01
 implementation group: 'cz.msebera.android', name: 'httpclient', version: '4.4.1.2'
    implementation project (':jsoup-1.10.2')

Se poate crea un advertisement de serviciu sub Linux prin crearea unui fișier de configurare, folosiți un alt nume în loc de perfectdesktop:

$ cat   /etc/avahi/services/chat.service 
<?xml version="1.0" standalone='no'?>
<!DOCTYPE service-group SYSTEM "avahi-service.dtd">
<service-group>
  <name>Chat-perfectdesktop</name>
  <service>
    <type>_chatservice._tcp</type>
    <port>5003</port>
  </service>
</service-group>
Se restartează severul de zeroconf cu comanda

# /etc/init.d/avahi-daemon restart
Apoi se poate verifica cu tcpdump traficul specific DNS-SD care este generat:

#tcpdump -ni eth0 -s0 -w 'file1.pcap' 'udp port 5353'
Traficul capturat în file1.pcap poate fi examinat cu wireshark file1.pcap. Dacă ați rulat tcpdump pe telefon, este necesară aducerea fișierului .pcap pe desktop cu jurorul comanzii adb pull /sdcard/file1.pcap .

Din alt terminal, se poate lansa o căutare de servicii de tipul chatservice:

#avahi-browse -rk _chatservice._tcp
Iar în tcpdump se observă primirea query-urilor pentru serviciu, și răspunsul desktop-ului. Atenție, acesta nu este un serviciu care rulează pe portul 5003, ci doar anunțul(advertisement) pentru serviciu.
