## Localize your application content

- This program will read from your application string.xml file and generate translated strings.xml files in your
  preferred languages using google sheet.
- Why using google sheet?
- To avoid high transaction charges on API requests for translation. Or spending money on a Translator. 😉

## Prerequisites

To run this quickstart, you need the following
prerequisites:</br> https://developers.google.com/sheets/api/quickstart/java#prerequisites

1. To create a project and enable an API,
    - NOTE : For this quickstart, you are enabling the “Google Sheets API”

2. Copy the credentials.json and add in project directory (StringLocalizationGenerator/credentials.json)

## Download

Download jar file of [Android-Localization](lib/Android-Localization.jar)
</br>NOTE : if you directly run jar, please put credentials.json file in the folder where your jar file exists.

## How to Run .jar file.

     * cmd : java -jar <jar-file-name>.jar

After executing .jar file or directly run program

1. Please enter google sheet url
2. Please enter google sheet name
3. Please enter language code (i.e. German=de,French(Standard)=fr, French(Belgium)=fr-be)
4. Please enter 'String.xml' file path (i.e.D:\string.xml)

- After performing 4th step : Please check google sheet( https://docs.google.com/spreadsheets/d/ 'ID OF GOOGLE
  SHEET'/edit#gid=0 ) for all strings are converted or not, then perform 5th step

5. For download translated language files type 'downloadFiles'

## NOTE

     1. Google sheet must be public with read and write permission.
     2. Default language should be English.
     3. First time,after performing the 4th step its redirecting browser and showing a popup of quickstart for permission so click with continue button.
     