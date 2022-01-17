<h1>Dizzy HAZU Downloader</h1>

Small tool for downloading / OCR and PDF export of books from academia archive
<br>Program will parse provided url and pull all document pages, download them and build PDF file (and perform OCR) for easier reading.

Requirements
Java JRE min version 1.11
<br>[OpenJDK](https://openjdk.java.net/).
<br>[OracleJRE](https://www.oracle.com/technetwork/java/javase/downloads/index.html). 
<br>[Visual C++ Redistributable 2015](https://www.microsoft.com/en-us/download/details.aspx?id=48145). needed for Tesseract OCR

Project uses
<br>[Tesseract Open Source OCR Engine](https://github.com/tesseract-ocr/tesseract).
<br>[Java JNA wrapper for Tesseract OCR API](https://github.com/nguyenq/tess4j).

<br><b>Usage</b>
<br>
<br><b>OCR initialization</b>
<br>Before doing any OCR application needs to download trained models from: https://github.com/tesseract-ocr/tessdata_best/.
<br> Initialization is needed due to need for downloading additional files for ocr purpose 
<br>
<code>java -jar dizzy.jar init-ocr-basic</code>
<br>
<br>
<b>Download document and export with text integrated in PDF</b>
<br>
<code>java -jar dizzy.jar https://dizbi.hazu.hr/a/?pr=i&id=1958143
</code>
