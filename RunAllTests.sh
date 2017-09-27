#!/bin/sh

pip install -U selenium

if [ -f chromedriver ]; then
	PATH=$PATH:./chromedriver
fi

python WebTestCrawler.py
