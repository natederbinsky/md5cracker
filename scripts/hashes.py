#!/usr/bin/env python

import sys
import os
import getopt

import requests
from bs4 import BeautifulSoup
from urllib.parse import urljoin

def _get(page):
    return requests.get(urljoin("http://www.passwordrandom.com/most-popular-passwords/page/", page))

def _p(s):
    print(s, end='')

def main(argv):
    
    n = 20
    
    _p("{")
    for i in range(n):
        html = _get(str(i+1)).text
        soup = BeautifulSoup(html, "html.parser")
        stuff = soup.find("table", {"class":"table", "id":"cntContent_lstMain"}).find_all("tr")[1:]
    
    
        for tag in stuff:
            info = tag.find_all("td")
            _p('"{}",'.format(info[1].text))
        print()
    print("};")
    
    _p("{")
    for i in range(n):
        html = _get(str(i+1)).text
        soup = BeautifulSoup(html, "html.parser")
        stuff = soup.find("table", {"class":"table", "id":"cntContent_lstMain"}).find_all("tr")[1:]
        
        
        for tag in stuff:
            info = tag.find_all("td")
            _p('"{}",'.format(info[2].text.upper()))
        print()
    print("};")
    
if __name__ == "__main__":
    main(sys.argv[1:])
