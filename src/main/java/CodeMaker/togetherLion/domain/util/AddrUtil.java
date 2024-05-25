package CodeMaker.togetherLion.domain.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

@Component
public class AddrUtil {

    @Value("${kakao.rest.api}")
    private String restApi;


    /**
     * 경위도 정보로 주소를 불러오는 메소드
     */
    public String coordToAddr(String longitude, String latitude){
        String url = "https://dapi.kakao.com/v2/local/geo/coord2address.json?x="+longitude+"&y="+latitude;
        String addr = "";
        try{
            addr = getRegionAddress(getJSONData(url));
            //LOGGER.info(addr);
        }catch(Exception e){
            System.out.println("주소 api 요청 에러");
            e.printStackTrace();
        }
        return addr;
    }

    /*
     *  경위도 정보로 법정동 불러오는 메소드 : 지수
     * */
    public String coordToR1D(String longitude, String latitude) {
        String url = "https://dapi.kakao.com/v2/local/geo/coord2address.json?x="+longitude+"&y="+latitude;
        String addr = "";
        try{
            addr = getRegion1Depth(getJSONData(url));
            //LOGGER.info(addr);
        }catch(Exception e){
            System.out.println("주소 api 요청 에러");
            e.printStackTrace();
        }
        return addr;
    }

    public String coordToR2D(String longitude, String latitude) {
        String url = "https://dapi.kakao.com/v2/local/geo/coord2address.json?x="+longitude+"&y="+latitude;
        String addr = "";
        try{
            addr = getRegion2Depth(getJSONData(url));
            //LOGGER.info(addr);
        }catch(Exception e){
            System.out.println("주소 api 요청 에러");
            e.printStackTrace();
        }
        return addr;
    }

    public String coordToR3D(String longitude, String latitude) {
        String url = "https://dapi.kakao.com/v2/local/geo/coord2address.json?x="+longitude+"&y="+latitude;
        String addr = "";
        try{
            addr = getRegion3Depth(getJSONData(url));
            //LOGGER.info(addr);
        }catch(Exception e){
            System.out.println("주소 api 요청 에러");
            e.printStackTrace();
        }
        return addr;
    }

    /**
     * REST API로 통신하여 받은 JSON형태의 데이터를 String으로 받아오는 메소드
     */
    private String getJSONData(String apiUrl) throws Exception {
        HttpURLConnection conn = null;
        StringBuffer response = new StringBuffer();

        //인증키 - KakaoAK하고 한 칸 띄우기
        String auth = "KakaoAK " + restApi;

        //URL 설정
        URL url = new URL(apiUrl);

        conn = (HttpURLConnection) url.openConnection();

        //Request 형식 설정
        conn.setRequestMethod("GET");
        conn.setRequestProperty("X-Requested-With", "curl");
        conn.setRequestProperty("Authorization", auth);

        //request에 JSON data 준비
        conn.setDoOutput(true);

        //보내고 결과값 받기
        int responseCode = conn.getResponseCode();
        if (responseCode == 400) {
            System.out.println("400:: 해당 명령을 실행할 수 없음");
        } else if (responseCode == 401) {
            System.out.println("401:: Authorization가 잘못됨");
        } else if (responseCode == 500) {
            System.out.println("500:: 서버 에러, 문의 필요");
        } else { // 성공 후 응답 JSON 데이터받기

            Charset charset = Charset.forName("UTF-8");
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));

            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
        }

        //System.out.println(response.toString());

        return response.toString();
    }

    /**
     * JSON형태의 String 데이터에서 주소값(address_name)만 받아오기
     */
    private String getRegionAddress(String jsonString) {
        String value = "";
        JSONObject jObj = (JSONObject) JSONValue.parse(jsonString);
        JSONObject meta = (JSONObject) jObj.get("meta");
        long size = (long) meta.get("total_count");

        if(size>0){
            JSONArray jArray = (JSONArray) jObj.get("documents");
            JSONObject subJobj = (JSONObject) jArray.get(0);
            JSONObject roadAddress =  (JSONObject) subJobj.get("road_address");

            if(roadAddress == null){
                JSONObject subsubJobj = (JSONObject) subJobj.get("address");
                value = (String) subsubJobj.get("address_name");
            }else{
                value = (String) roadAddress.get("address_name");
            }

            if(value.equals("") || value==null){
                subJobj = (JSONObject) jArray.get(1);
                subJobj = (JSONObject) subJobj.get("address");
                value =(String) subJobj.get("address_name");
            }
        }
        return value;
    }

    /**
     * JSON형태의 String 데이터에서 region_1depth_name(경기도)만 받아오기
     */
    private String getRegion1Depth(String jsonString) {
        String value = "";
        JSONObject jObj = (JSONObject) JSONValue.parse(jsonString);
        JSONObject meta = (JSONObject) jObj.get("meta");
        long size = (long) meta.get("total_count");

        if(size>0){
            JSONArray jArray = (JSONArray) jObj.get("documents");
            JSONObject subJobj = (JSONObject) jArray.get(0);
            JSONObject roadAddress =  (JSONObject) subJobj.get("address");

            if(roadAddress == null){
                JSONObject subsubJobj = (JSONObject) subJobj.get("road_address");
                value = (String) subsubJobj.get("region_1depth_name");
            }else{
                value = (String) roadAddress.get("region_1depth_name");
            }

            if(value.equals("") || value==null){
                subJobj = (JSONObject) jArray.get(1);
                subJobj = (JSONObject) subJobj.get("road_address");
                value =(String) subJobj.get("region_1depth_name");
            }
        }
        return value;
    }

    /**
     * JSON형태의 String 데이터에서 region_2depth_name(성남시 분당구)만 받아오기
     */
    private String getRegion2Depth(String jsonString) {
        String value = "";
        JSONObject jObj = (JSONObject) JSONValue.parse(jsonString);
        JSONObject meta = (JSONObject) jObj.get("meta");
        long size = (long) meta.get("total_count");

        if(size>0){
            JSONArray jArray = (JSONArray) jObj.get("documents");
            JSONObject subJobj = (JSONObject) jArray.get(0);
            JSONObject roadAddress =  (JSONObject) subJobj.get("address");

            if(roadAddress == null){
                JSONObject subsubJobj = (JSONObject) subJobj.get("road_address");
                value = (String) subsubJobj.get("region_2depth_name");
            }else{
                value = (String) roadAddress.get("region_2depth_name");
            }

            if(value.equals("") || value==null){
                subJobj = (JSONObject) jArray.get(1);
                subJobj = (JSONObject) subJobj.get("road_address");
                value =(String) subJobj.get("region_2depth_name");
            }
        }
        return value;
    }

    /**
     * JSON형태의 String 데이터에서 region_3depth_name(삼평동)만 받아오기
     */
    private String getRegion3Depth(String jsonString) {
        String value = "";
        JSONObject jObj = (JSONObject) JSONValue.parse(jsonString);
        JSONObject meta = (JSONObject) jObj.get("meta");
        long size = (long) meta.get("total_count");

        if(size>0){
            JSONArray jArray = (JSONArray) jObj.get("documents");
            JSONObject subJobj = (JSONObject) jArray.get(0);
            JSONObject roadAddress =  (JSONObject) subJobj.get("address");

            if(roadAddress == null){
                JSONObject subsubJobj = (JSONObject) subJobj.get("road_address");
                value = (String) subsubJobj.get("region_3depth_name");
            }else{
                value = (String) roadAddress.get("region_3depth_name");
            }

            if(value.equals("") || value==null){
                subJobj = (JSONObject) jArray.get(1);
                subJobj = (JSONObject) subJobj.get("road_address");
                value =(String) subJobj.get("region_3depth_name");
            }
        }
        return value;
    }
}
