RSSReader
=========

RSS reader is a simple RSS parser uses jsoup to parse the feed.<br>
The parser reads the &lt;title&gt;, &lt;pubDate&gt;, &lt;description&gt; and &lt;guid&gt; and writes them into four different ArrayList.<br>
After returns in a ListView and each item and opens a AlertDialog with a WebView where loads the title, date and description.<br>
The AlertDialog also has two buttons to open the URL in the browser and to share the item.
