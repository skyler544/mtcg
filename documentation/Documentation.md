


# Design

The application is structured in layers. Each layer has an area of concern for  
which it should be responsible, with as little mixing as possible between the  
layers. Furthermore, any one layer should only touch the layer immediately  
beneath it, and never any deeper than a single layer. The layer hierarchy is as  
follows:  

-   Controllers  
    -   Services  
        -   Models
        -   Repositories


## Controllers

Controllers are responsible for receiving Http Requests and creating and sending  
Http Responses. The pattern is simple:  

1.  Get a request object
2.  Authenticate the user (via the `UserService`)
3.  Retrieve the contents of the response from the service layer
4.  Send the response


## Services

Services are responsible for all the logic that happens in the  
application. Methods are provided for i.e. authentication, trading, and  
battling. The pattern here is more variable but follows this general list of objectives:  

-   Sanity checking of data
-   Usage of repository methods for input/output from the database
-   Performance of some kind of operation on the data such as calculating a user's stats
-   Returning valid data


## Models

Models define the actual "objects" involved in the application. These can be  
more or less concrete, but they serve a couple of purposes:  

-   Structuring data
-   Defining objects


## Repositories

Repositories depend on the models for defining what the database schema should  
look like. The repository then provides simplistic database IO, including  
creating, reading, updating, and deleting data. (CRUD)  

-   Defines schema
-   Provides input/output
-   Data validation, in the sense that only data which conforms to the models can  
    be read/inserted/etc.


# Lessons Learned

This project was an excellent learning experience. I now feel confident in my  
understanding of two important concepts in the world of software engineering:  

1.  REpresentational State Transfer (REST)
2.  Model View Controller architecture

Learning these topics has already improved my skills in a professional context.  

As time went on during the development of this project, I realised again and  
again that I had made mistakes or suboptimal architectural decisions in previous  
parts of the application. This should be apparent to anyone looking at the code  
in the order I wrote it: Users -> Cards -> Trading -> Battles  

I had to prevent myself from going back and trying to fix those issues in order  
to finish the project on time. I would like to rewrite this entire project  
(potentially in another language) someday with two goals: Cementing the lessons  
learned, and improving the architecture from the ground up.  

Rewriting it in another language is no comment on the effectiveness of Java;  
this application is simply, in general, large enough to make acquainting myself  
with more of the features of another useful language (like PHP) a neat sub-goal  
of rewriting it.  

Something that I found sorely lacking while developing this application was  
regular code reviews with more experienced colleagues. I'm certain that much of  
the code I wrote has potential to be broken in ways I never even considered, or  
that it doesn't conform to accepted Java "best practices," or that it's wildly  
inefficient, or any number of other problems. This is not an excuse for poor  
code quality, but it is food for thought.  

One other note: I found writing meaningful unit tests to be the most confusing  
part of the development. This is an area where I need to improve.  


# Time Spent

Note: This information is only approximate; there were times when I worked on it  
without recording the time spent, and there were times where the clock was  
running while I took a break, etc.  

Furthermore, for simplicity, I didn't split up the time spent working on  
Controllers and Services, or from one Controller to another. I decided that  
constantly worrying about the running clock and which context it was in wasn't  
worth the mental effort.  

I categorized the Controllers, Services, Repositories, and Models into roughly  
four areas according to the OpenAPI specification:  

1.  Users
2.  Cards
3.  Trading
4.  Battle

I wrote them in this order, and mostly worked on the Repositories and Models  
first, then the Controllers, and finally the Services.  

I spent more time (on the order of maybe four hours) on the Database,  
Synchronization, and Unit Test categories than is recorded here in particular,  
as I often needed to research these topics in order to understand certain  
implementation details and strategies.  

<table border="2" cellspacing="0" cellpadding="6" rules="groups" frame="hsides">
<caption class="t-above"><span class="table-number">Table 1:</span> Clock summary at <span class="timestamp-wrapper"><span class="timestamp">[2023-02-24 Fri 13:37]</span></span></caption>

<colgroup>
<col  class="org-left" />

<col  class="org-left" />

<col  class="org-right" />
</colgroup>
<thead>
<tr>
<th scope="col" class="org-left">Headline</th>
<th scope="col" class="org-left">Time</th>
<th scope="col" class="org-right">&#xa0;</th>
</tr>
</thead>

<tbody>
<tr>
<td class="org-left"><b>Total time</b></td>
<td class="org-left"><b>2d 3:08</b></td>
<td class="org-right">&#xa0;</td>
</tr>
</tbody>

<tbody>
<tr>
<td class="org-left"><a href="./mtcg.pdf">Monster Card Game</a></td>
<td class="org-left">2d 3:08</td>
<td class="org-right">&#xa0;</td>
</tr>


<tr>
<td class="org-left">&ensp;&ensp;Database [4/4]</td>
<td class="org-left">&#xa0;</td>
<td class="org-right">5:59</td>
</tr>


<tr>
<td class="org-left">&ensp;&ensp;Controllers [4/4]</td>
<td class="org-left">&#xa0;</td>
<td class="org-right">1d 12:29</td>
</tr>


<tr>
<td class="org-left">&ensp;&ensp;Unique Feature [1/1]</td>
<td class="org-left">&#xa0;</td>
<td class="org-right">1:10</td>
</tr>


<tr>
<td class="org-left">&ensp;&ensp;Synchronization [2/2]</td>
<td class="org-left">&#xa0;</td>
<td class="org-right">2:13</td>
</tr>


<tr>
<td class="org-left">&ensp;&ensp;Learn to use Unit Tests [1/1]</td>
<td class="org-left">&#xa0;</td>
<td class="org-right">4:10</td>
</tr>


<tr>
<td class="org-left">&ensp;&ensp;Documentation [4/4]</td>
<td class="org-left">&#xa0;</td>
<td class="org-right">1:07</td>
</tr>
</tbody>
</table>

All of this information was generated from the project progress tracking file,  
[mtcg.org](mtcg.md) The git history of that file provides a nice overview of how  
development was structured and accomplished.  


# Link to git

[MTCG](https://github.com/skyler544/mtcg)  

