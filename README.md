# (unofficial) Telefonterror.no Android application # 

This is an application for Android based mobile devices. 

## Purpose ## 

Its purpose is to give users on Android devices that live in Norway a 
way to identify spammers and telemarketers that try to call them. The 
identification of these unwanted people is done with the very nice 
database from [Telefonterror.no][1]. 

## Database copyright ## 

The author of this application has acquired permission to use the data 
for this specific purpose. If you plan to develop this codebase into 
something else that uses that database, you'll need to acquire 
permission from the rightsholder of [Telefonterror.no][1]. Publishing 
this application on the Android Marketplace without consent from 
telefonterror.no is considered such an activity. 

## Function ## 

When you open the application you are greeted with two buttons. The 
first button will open up your inbound and missed calls log. It is 
filtered to only show numbers that are not in your contact list. Any 
number that is not on a blacklist is listed with a yellow background. 
Any number that is blacklisted is shown with a red background. In 
addition the type of company/institution it is is listed before the 
number, and the name of the institution is listed last. It is usually 
better to view this list in landscape mode, as it gives more space for 
the name of the blacklisted entry. 

The second button updates the blacklist. Please be advised that the 
blacklist is quite large and can take a substantial time to load. It 
contains (currently) about 2500 entries. Updating the blacklist on a 
real phone (compared to the emulator) can take a few minutes. If you 
interrupt the application during this process you will have to start 
over again. 

The application only requires Internet access while you're updating the 
blacklist. 

## Technical details ## 

The application exposes a ContentProvider which allows you to query the 
blacklist. There are two separate blacklist, a public one (downloaded 
with the front page button), and a private one. They are accessed via 
the same provider, but with different content uris. 

## Future ## 

There is currently no way to update the private blacklist, but this is 
planned for a future version. We are also planning to add features that 
will display the blacklist status of a number when it calls you. We are 
also planning to add a feature to submit a number to the public 
blacklist and report that you were contacted by an existing blacklisted 
number. 

## Author ## 

Copyright (c) 2010 Robin Smidsrød <robin@smidsrod.no> 

[1]: http://telefonterror.no/ "Telefonterror.no, driftet av Bjarte Aune Olsen" 
