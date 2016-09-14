<html>
<body>
<div>
   <#include "includes/header.ftl">
</div>
<h3>${greeting}</h3>
<p>
   Use the link below to reset your password. This link will expire in 24 hours. Thank you!
</p>
<p><strong><a href="${resetLink}">Reset Your Password</a></strong></p>
<p style="margin-top:60px; font-size=smaller;font-style: italic;">
    A message from ${memberServices}
</p>
<div>
    <#include "includes/footer.ftl">
</div>
</body>
</html>

