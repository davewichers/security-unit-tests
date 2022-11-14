#!/bin/sh

pip3 install -U selenium

if [ -f chromedriver ]; then
	export PATH=$PATH:./chromedriver
fi

python3 WebTestCrawler.py
