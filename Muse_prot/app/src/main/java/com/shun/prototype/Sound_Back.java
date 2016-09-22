package com.shun.prototype;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

public class Sound_Back extends Activity {
    static final int RESULT = 1000;
    int sound[]={0,0,0,0};//データ保存用変数
    int beat=100;
    int edit=0;
    int songNo=0;

    // 楽器リスト
    int Arrange1InstList[] = new int[3];
    int Arrange2InstList[] = new int[3];
    int melodyInstList[] = new int[4];

    private GraphicView graphicView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soundback);

        Intent intent=getIntent();
        songNo = intent.getIntExtra("SongNo", 0);
        Arrange1InstList[0] = intent.getIntExtra("A1Inst0", 0);
        Arrange1InstList[1] = intent.getIntExtra("A1Inst1", 0);
        Arrange1InstList[2] = intent.getIntExtra("A1Inst2", 0);
        Arrange2InstList[0] = intent.getIntExtra("A2Inst0", 0);
        Arrange2InstList[1] = intent.getIntExtra("A2Inst1", 0);
        Arrange2InstList[2] = intent.getIntExtra("A2Inst2", 0);
        melodyInstList[0] = intent.getIntExtra("MInst0", 0);
        melodyInstList[1] = intent.getIntExtra("MInst1", 0);
        melodyInstList[2] = intent.getIntExtra("MInst2", 0);
        melodyInstList[3] = intent.getIntExtra("MInst3", 0);
        beat = intent.getIntExtra("BEAT", 0);

        //GraphicViewのオブジェクト生成
        graphicView = new GraphicView(this);
        setContentView(graphicView);
        graphicView.setBpm(beat/2);
        graphicView.setScene(false);
        graphicView.onResume();

    }

    protected void onActivityResult(int requestCode,int resultCode,Intent intent){
        super.onActivityResult(requestCode,resultCode,intent);
        if(resultCode==RESULT_OK  && requestCode==RESULT && null!=intent){
	        //データの受取と反映
	        Arrange1InstList[0] = intent.getIntExtra("RES_A1Inst0", 0);
	        Arrange1InstList[1] = intent.getIntExtra("RES_A1Inst1", 0);
	        Arrange1InstList[2] = intent.getIntExtra("RES_A1Inst2", 0);
	        Arrange2InstList[0] = intent.getIntExtra("RES_A2Inst0", 0);
	        Arrange2InstList[1] = intent.getIntExtra("RES_A2Inst1", 0);
	        Arrange2InstList[2] = intent.getIntExtra("RES_A2Inst2", 0);
	        melodyInstList[0] = intent.getIntExtra("RES_MInst0", 0);
	        melodyInstList[1] = intent.getIntExtra("RES_MInst1", 0);
	        melodyInstList[2] = intent.getIntExtra("RES_MInst2", 0);
	        melodyInstList[3] = intent.getIntExtra("RES_MInst3", 0);

	        // テンポ処理
	        beat = intent.getIntExtra("RES_BEAT", 0);
	        if( beat < 20 ) beat = 20;
	        else if( beat > 240 ) beat = 240;

	        // midi更新
	        //Sound_Front.changeMidiFile();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() { super.onStop(); }

    @Override
    //タッチイベントを拾う
    public boolean onTouchEvent(MotionEvent motionEvent)
    {
        float getx=motionEvent.getX();//タッチ座標取得
        float gety=motionEvent.getY();
        float nowx[]=graphicView.getBxpoint();//画像がおいてある場所取得
        float nowy[]=graphicView.getBypoint();

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN://押した時
                Log.d("", "ACTION_DOWN");
                Log.d("", "EventLocation X:" + getx + ",Y:" + gety);
                break;
            case MotionEvent.ACTION_UP://離した時
                Log.d("", "ACTION_UP");
                long eventDuration2 = motionEvent.getEventTime() - motionEvent.getDownTime();
                Log.d("", "eventDuration2: " +eventDuration2+" msec");//デバッグ用出力
                Log.d("", "Pressure: " + motionEvent.getPressure());
                Log.d("", "edit="+edit);
                Log.d("","getx   ="+getx+",gety   ="+gety);
                Log.d("","nowx[0]="+nowx[0]+",nowy[0]="+nowy[0]);


                if(getx<60 && gety<120) {//座標判定
                    Intent intent1 = new Intent(getApplication(), Option.class);
                    intent1.putExtra("S1", sound[0]);//各データの転送
                    intent1.putExtra("S2", sound[1]);
                    intent1.putExtra("S3", sound[2]);
                    intent1.putExtra("S4", sound[3]);
                    intent1.putExtra("BEAT", beat);
                    intent1.putExtra("SONGNO",songNo);
                    int requestCode = RESULT;

                    //Sound_Front.mediaPlayer.pause();
                    startActivityForResult(intent1, requestCode);//オプションに飛ぶ
                }

                else if(getx>1090 && gety<120){
                    Intent intent2 = new Intent();
                    intent2.putExtra("RES_S1",sound[0]);//データを返す
                    intent2.putExtra("RES_S2",sound[1]);
                    intent2.putExtra("RES_S3",sound[2]);
                    intent2.putExtra("RES_S4",sound[3]);
                    intent2.putExtra("RES_BEAT",beat);
                    intent2.putExtra("RES_SONGNO",songNo);
                    setResult(RESULT_OK,intent2);
                    finish();//表に戻る
                }

                else if(getx>nowx[0]+20 && getx<nowx[0]+90 && gety>nowy[0]+60 && gety<nowy[0]+130){
                    graphicView.setBxpoint(0,-1);//画像1の消去
                    graphicView.setBypoint(0,-1);
                }
                else if(getx>nowx[1]+20 && getx<nowx[1]+90 && gety>nowy[1]+60 && gety<nowy[1]+130){
                    graphicView.setBxpoint(1,-1);//画像2の消去
                    graphicView.setBypoint(1,-1);
                }
                else if(getx>nowx[2]+20 && getx<nowx[2]+90 && gety>nowy[2]+60 && gety<nowy[2]+130){
                    graphicView.setBxpoint(2,-1);//画像3の消去
                    graphicView.setBypoint(2,-1);
                }
                else if(getx>nowx[3]+20 && getx<nowx[3]+90 && gety>nowy[3]+60 && gety<nowy[3]+130){
                    graphicView.setBxpoint(3,-1);//画像4の消去
                    graphicView.setBypoint(3,-1);
                }
                else if(getx>nowx[4]+20 && getx<nowx[4]+90 && gety>nowy[4]+60 && gety<nowy[4]+130){
                    graphicView.setBxpoint(4,-1);//画像5の消去
                    graphicView.setBypoint(4,-1);
                }

                else if(getx<60 && gety>430 && gety<929){
                    edit=((int)gety-430)/100;//編集する画像の選択
                }

                else if(getx<60 && gety>930 && gety<1010){
                    for(int i=0;i<5;i++) {//全画像消去
                        graphicView.setBxpoint(i, -1);
                        graphicView.setBypoint(i, -1);
                    }
                }

                else{
                    graphicView.setBxpoint(edit,getx-20);//タッチ位置に画像配置
                    graphicView.setBypoint(edit,gety-60);
                }

                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("", "ACTION_MOVE");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d("", "ACTION_CANCEL");
                break;
        }

        return false;
    }
}
