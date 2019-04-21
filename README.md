# GenerateHTMLForGITReports
Little app to generate a HTML file from a git log.
This application will generate an html file with the number of commits by author and percentage, the total commit count and the commit's history for the git log.

## Run
### Generate report file
``` git log --graph > report.txt ```
### Run program
``` java -jar GenerateHTMLForGITReport.jar ```
It will ask for the report file as "origin" and the output file as "destination". For example: ``` report.txt ``` for origin and ``` report.html ``` for destination. It'll also ask for the project name.
