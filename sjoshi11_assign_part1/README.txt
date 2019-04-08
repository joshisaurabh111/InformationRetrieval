This is the content of makefile that runs the code:

default:
	python assignment_part1.py $(DIR) 

You can pass filename as parameter as shown below (DIR is capital):

make DIR=<"filename"> 

Special Comments:

1.	Cannot split on basis of '.'(period) since there are number that have a dot in between them and also some email address has a dot 		example@xyz.com 
	Split is done on basis of '@'. So email example@xyz.com will be split as "example" and "xyz.com"

2.	The footer data is also extracted. Cannot detect the difftence since it is text (alphanumeric).

3.	Special characters at the end or beginning of numbers are also deleted with the regex. +2 becomes 2.

4. 	The plural words that are intermediate in a string are not singularized.
	rec.arts.movies.reviews
	For the above string only reviews --> review

5. 	"you'll" becomes youll after removing the apostrophe. It is not parsed as "you" or "will"
