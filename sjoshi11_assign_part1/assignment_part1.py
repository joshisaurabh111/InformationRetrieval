import sys
from urllib import urlopen
from bs4 import BeautifulSoup
from collections import Counter
import pandas as pd
import re


def init():
	#opening html file passing as parameter to BeautifulSoup
	url = sys.argv[1]
	html = urlopen(url).read()
	
	#parsing as html file
	soup =  BeautifulSoup(html, "lxml")
	
	#Extracting out the HTML and converting to plain text
	for script in soup(["script", "style"]):
		script.extract()    # rip it out
	
	# get text
	text = soup.get_text()
	
	# break into lines and remove leading and trailing space on each
	lines = (line.strip()
		for line in text.splitlines()
		)
		
	# break multi-headlines into a line each
	chunks = (phrase.strip() for line in lines for phrase in line.split("  "))
	
	# drop blank lines
	text = '\n'.join(chunk for chunk in chunks if chunk)
	
	#converting unicode result to String
	text = text.encode("ascii", "ignore")
	
	return text

#tokenizing the string into words definition
def tokenize() :
	if text is not None:
		#replace special characters as spaces and split and put in a list (words)
		words = text.lower().replace("-"," ").replace("@"," ").replace("("," ").replace(")"," ").replace("!"," ").replace("/"," ").replace("'","").split()
		return words
	else :
		return None
		

#printing content in an output file
def printList() :
	#removing special characters at the start and end of each string in the list
	for idx, word in enumerate(words):
		word = re.sub(r'(?<!\S)[^\s\w]+|[^\s\w]+(?!\S)', '', word)
		words[idx] = word
	#list of elements to be deleted as stop words	
	delete = ["and","a","the","an","by","from","for","hence","of","the","with","in","within","who","when","where","why","how","whom","have","had","has","not","for","but","do","does","done"]
	
	#deleting single characters that are not numbers
	for i in words:
		if len(i) < 2 and i.isalpha():
			words.remove(i) 
			
	for idx, i in enumerate(words):
		if i.endswith('ies') and not i.endswith("eies") or i.endswith("aies"):
			i = i[:-3]+'y'
			words[idx] = i
		elif i.endswith('es') and not i.endswith("ees") or i.endswith("aes") or i.endswith("oes"):
			i = i[:-2]+'e'
			words[idx] = i
		elif i.endswith('s') and not i.endswith("us") or i.endswith("as") or i.endswith("ss"):
			i = i[:-1]+''
			words[idx] = i
			
					
	#Counter to club multiple occurances of the same word		
	counts = Counter(word for word in words if word not in delete)
	
	#print "\n".join('{}'.format(k) for k,v in counts.items())
	
	#File writing operation with sorting in lexicographical order
	opFile = open("dictionary.txt", "w+")
	opFile.write("\n".join('{}'.format(k) for k,v in sorted(counts.items())))
	opFile.close()
	return None
	

#call to the 3 functions defined above
text = init()
words = tokenize()
printList()

