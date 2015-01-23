package com.huoqiu.widget.filedownloader;

import android.app.Activity;

public class TestDownload extends Activity {
//	private LinearLayout rootLayout;
//	private EditText et;
//	Button btn2;
//	FileDownloader mFileDownloader;
//	LinearLayout progressLayout;
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.downloadlayout);
//		// 动�?生成新View，获取系统服务LayoutInflater，用来生成新的View
//		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//		rootLayout = (LinearLayout) findViewById(R.id.root);
//		et = (EditText) findViewById(R.id.path);
//		et.setVisibility(View.GONE);
//		Button btn = (Button) findViewById(R.id.btn1);
//		btn.setOnClickListener(onClickListener);
//		btn2 = (Button) findViewById(R.id.btn2);
//		btn2.setOnClickListener(onClickListener);
//
//		// DownloadFileDB df = new DownloadFileDB(this);
//		// List<String> list = df.queryUndone();
//		// for(String str:list){
//		// createDownload(str);
//		// }
//
//	}
//
//	private OnClickListener onClickListener = new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//			
//			// TODO Auto-generated method stub
//			switch (v.getId()) {
//			case R.id.btn1:
//				String str = "http://61.50.254.57:8088/nature-person/mobilenature/download/NaturalSaler_common.apk";
//				download(str);
//				break;
//			case R.id.btn2:
//				if("resume".equals(btn2.getText())){
//					btn2.setText("stop");
//					mFileDownloader.pause();
//				} else {
//					mFileDownloader.resume();
//				}
//				break;
//
//			default:
//				break;
//			}
//		}
//	};
//
//	/**
//	 * 下载按钮
//	 * 
//	 * @param view
//	 */
//	public void download(String path) {
//		createDownload(path);
//	}
//
//	LinearLayout childLayout;
//
//	void createDownload(String path) {
//		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//		progressLayout = (LinearLayout) inflater.inflate(
//				R.layout.downloadprogress, null);
//		childLayout = (LinearLayout) progressLayout.getChildAt(0);
//		ProgressBar bar = (ProgressBar) childLayout.findViewById(R.id.progress);
//		TextView text = (TextView) childLayout.findViewById(R.id.textview);
//		try {
//			FileDownloadListener fdl = new FileDownloadListener(
//					getApplicationContext(), bar, text, path, callBack);
//			// 调用当前页面中某个容器的addView，将新创建的View添加进来
//			rootLayout.addView(progressLayout);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	FileDownloadListener.DownloadCallBack callBack = new FileDownloadListener.DownloadCallBack() {
//
//		@Override
//		public void downloadSuccess() {
//			// TODO Auto-generated method stub
//			// 下载完成后�?出进度条
//			rootLayout.removeView(progressLayout);
//		}
//
//		@Override
//		public void downloadFail() {
//			// TODO Auto-generated method stub
//			Toast.makeText(TestDownload.this, "下载过程中出现错�?, Toast.LENGTH_SHORT)
//					.show();
//		}
//	};
//
}
