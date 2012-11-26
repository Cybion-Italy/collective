## README file for Knowledge Room install into iCommunity platform

This directory contains the files needed for the Knowledge Room. 
Technically, it is NOT a Wordpress plugin, rather it is a Wordpress Page Template. 
The file kr-root.php is the main file, and includes kr-main.php that dynamically loads the different pages included in kr-modules directory. 

## REQUIREMENTS
Note: We tested the integration internally, on a server with a fresh install of the following components: 
Wordpress 3.2.1
BuddyPress 1.5

## INSTALL
1. Copy the content of this directory into the theme that buddypress is using. 

In our case it is something like this: 
<wordpress installation directory>/wp-content/plugins/buddypress/bp-themes/bp-default/

Maybe iCommunity runs its own theme, so copy the directory content into the proper theme directory. 

2. Create a new page in the wordpress admin interface and set the title to 'Knowledge'. 
This is simply to ensure the permalink will be exactly 'knowledge', since in our code we use that name 
(we could improve this later if needed). 

In file kr-root.php we have something like this: 

[CODE]
include_once 'KnowledgeTemplateCybion.class.php';
global $knowledge_template;
$knowledge_template = new KnowledgeTemplateCybion();
[/CODE]

As you see, we use a KnowledgeTemplateCybion.class.php that implements the interface contained in KnowledgeTemplateInterface.php. 

At deploy time, Ubitech should be able to write its own implementation of the interface, with the proper implementation of methods using wordpress calls and representing data with the proper classes as seen in KnowledgeTemplateCybion.class.php. 
After implementing that interface, the file kr-root should be changed like this: 

[CODE]
include_once 'KnowledgeTemplateUbitech.class.php';
global $knowledge_template;
$knowledge_template = new KnowledgeTemplateUbitech();
[/CODE]

So that all the calls will use the ubitech instance.

3. In the platform you should be able to see the new page (if the menu is configured to show all the pages). 

Note: a development example is installed at http://cibionte.cybion.eu/icommunity/knowledge with the latest 
version of the Knowledge Room, to have an idea of how it should look like. 
Anyway, keep in mind that things can be a bit different. 
