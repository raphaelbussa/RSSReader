package rebus.rssreader;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Main extends Activity {

	private ArrayList<String> title;
	private ArrayList<String> description;
	private ArrayList<String> url;
	private ArrayList<String> data;

	private ProgressDialog loading;
	
	private String feed_rss = "http://feeds.bbci.co.uk/news/rss.xml";
	private ListView list;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		list = (ListView) findViewById(R.id.listView1);
		ParsingRSS parsingFeed = new ParsingRSS();
		parsingFeed.execute("");
	}

	private class ParsingRSS extends AsyncTask<String,String,String> {

		

		@Override
		protected void onPreExecute()
		{
			loading = new ProgressDialog(Main.this);
			loading.setMessage(getResources().getText(R.string.loading));
			loading.setCancelable(false);
			loading.show();
			title = new ArrayList<String>();
			description = new ArrayList<String>();
			url = new ArrayList<String>();
			data = new ArrayList<String>();
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				Document doc = Jsoup.connect(feed_rss)
						.userAgent("Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.172 Safari/537.22")
						.timeout((int) (9999 * 9999)).get();

				Elements elemento2 = doc.getElementsByTag("item");

				for(Element elemento3 : elemento2) {
					String title_parsed = elemento3.getElementsByTag("title").first().text();
					String url_parsed = elemento3.getElementsByTag("guid").first().text();
					String data_parsed = elemento3.getElementsByTag("pubDate").first().text();
					String description_parsed = elemento3.getElementsByTag("description").first().text();

					Log.d("title: ", title_parsed);
					Log.d("url: ", url_parsed);
					Log.d("data: ", data_parsed);
					Log.d("description: ", description_parsed);
					title.add(title_parsed);
					url.add(url_parsed);
					data.add(data_parsed);
					description.add(description_parsed);
				}

			} catch (Exception e) {
				Log.e("error", "parsing");
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			loading.dismiss();
			String[] list_title = title.toArray(new String[title.size()]);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(Main.this, android.R.layout.simple_list_item_1, list_title);
			list.setAdapter(adapter);
			list.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						final int pos, long id) {
					String article = data.get(pos) + "<br><br>" + description.get(pos);
					AlertDialog.Builder details = new AlertDialog.Builder(Main.this);
					details.setTitle(title.get(pos));
					WebView webview = new WebView(Main.this);
					webview.loadData(article, "text/html; charset=UTF-8", "UTF-8");
					details.setView(webview);
					details.setNegativeButton(getResources().getText(R.string.share), new DialogInterface.OnClickListener() {
					    @Override
					    public void onClick(DialogInterface dialog, int id) {
					    	Intent i=new Intent(android.content.Intent.ACTION_SEND);
							i.setType("text/plain");
							i.putExtra(android.content.Intent.EXTRA_TEXT, title.get(pos) + "\n\n" + url.get(pos));
							startActivity(Intent.createChooser(i,getResources().getText(R.string.share)));
					    }
					});
					details.setPositiveButton(getResources().getText(R.string.browser), new DialogInterface.OnClickListener() {
					    @Override
					    public void onClick(DialogInterface dialog, int id) {
					    	Intent openBrowser = new Intent(Intent.ACTION_VIEW);
					    	openBrowser.setData(Uri.parse(url.get(pos)));
							Main.this.startActivity(openBrowser);
					    }
					});
					details.show();
				}
			});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
		{
		case R.id.refresh:
			ParsingRSS parsingFeed = new ParsingRSS();
			parsingFeed.execute("");
			return true;
		}
		return true;

	}

}
