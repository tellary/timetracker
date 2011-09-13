//--------------------
jiraUrl = "http://sandbox.onjira.com/rpc/soap/jirasoapservice-v2";
jiraUsername = "tellarytest";
jiraPassword = "tellarytest";
//--------------------
jiraMapping = new MatchFirstJIRAMapping(
    new ProjectNameToIssueKeyJIRAMapping('Communication', 'TST-182'),
    new ProjectNameToIssueKeyJIRAMapping('Use elevator', 'TST-183').
        addProjectToComment(true),
    new ParseKeyJIRAMapping('TST')
)
//--------------------
date = "06-Sep-2011"
timeInOffice = '04:26:00'
togglCSV = "report.htm"
alreadyReported = 0
//--------------------