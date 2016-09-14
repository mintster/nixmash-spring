<#-- This is a freemarker comment which will not display -->
<#setting locale="de_DE">

<#include "includes/header.ftl">

${greeting}


|   User First Name: ${user.firstName <#-- This is a freemarker comment which will not display --> }
|
|   Details
|
|   User Last Name: ${user.lastName}

FREEMARKER SPECIAL VARIABLES:

Current Template Name: ${.currentTemplateName}
Freemarker Version: ${.version}
Locale: ${.locale}

<#include "includes/footer.ftl">