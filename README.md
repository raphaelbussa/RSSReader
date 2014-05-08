RSSReader
=========

RSS reader is a simple RSS parser uses jsoup to parse the feed. 
The parser reads the <title>, <pubDate>, <description> and <guid> and writes them into four different arraylist.
After returns in a ListView and each item and opens a AlertDialog with a WebView where loads the title, date and description.
The AlertDialog also has two buttons to open the URL in the browser and to share the item.
