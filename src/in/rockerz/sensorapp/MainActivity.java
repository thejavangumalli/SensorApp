package in.rockerz.sensorapp;

import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private Camera camera;
	boolean blink=true;
	Parameters p;
	Thread background;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		camera = Camera.open();
		p= camera.getParameters();
		TurnOnLight();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	protected void onStop() {
		super.onStop();
		if (camera != null) {
			camera.release();
		}
	}

	public void start(View v){
		final TextView tview=(TextView)findViewById(R.id.textView1);
		EditText text=(EditText)findViewById(R.id.time);
		int waitTime=Integer.parseInt(text.getText().toString());
		new CountDownTimer(waitTime*1000,1000){
			@Override
			public void onFinish() {
			background=new Thread(){

					@Override
					public void run() {
						while(!isInterrupted()){
							if(blink){
								p.setFlashMode(Parameters.FLASH_MODE_TORCH);
								camera.setParameters(p);
								camera.startPreview();
								blink=false;
							}
							else{
								p.setFlashMode(Parameters.FLASH_MODE_OFF);
								camera.setParameters(p);
								camera.stopPreview();
								blink=true;
							}
						}
						
					}
					
				};
			background.start();
			}

			@Override
			public void onTick(long millisUntilFinished) {
				Toast.makeText(getBaseContext(), "Turning on Disco Light", Toast.LENGTH_SHORT).show();	
				tview.setText("seconds remainig for disco light"+millisUntilFinished/1000);				
			}

		}.start();
	}
	public void stop(View v){
		background.interrupt();
		p.setFlashMode(Parameters.FLASH_MODE_OFF);
		camera.setParameters(p);
		camera.stopPreview();
	}
	public void close(View v){
		background.interrupt();
		finish();
	}
	public void TurnOnLight(){
		new CountDownTimer(5000, 1000){
			@Override
			public void onFinish() {
				p.setFlashMode(Parameters.FLASH_MODE_TORCH);
				camera.setParameters(p);
				camera.startPreview();
			}

			@Override
			public void onTick(long arg0) {
				Toast.makeText(getBaseContext(), "Turning on Light", Toast.LENGTH_SHORT).show();					
			}

		}.start();
	}
}
