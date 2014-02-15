package test.red.house;


//import android.content.Context;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.View;
//
//public class CalendarFragment extends Fragment{
//	@Override
//	public void onActivityCreated(Bundle savedInstanceState) {
//		super.onActivityCreated(savedInstanceState);
//		if (android.os.Build.VERSION.SDK_INT > 11){
//			CalendarView cal = (CalendarView) findViewById(R.layout.calendar_layout);
//	        
//	        cal.setOnDateChangeListener(new OnDateChangeListener() {
//				
//			@Override
//			public void onSelectedDayChange(CalendarView view, int year, int month,
//					int dayOfMonth) {
//				// TODO Auto-generated method stub
//				
//				Toast.makeText(getBaseContext(),"Selected Date is\n\n"
//					+dayOfMonth+" : "+month+" : "+year , 
//					Toast.LENGTH_LONG).show();
//			}
//		});
//		}
//	}
//	@Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.activity_main, menu);
//        return true;
//    }
//}
