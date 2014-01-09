ImageSearchEngine
=================
Build it by Wangwei[weiransoft#126.com]

 ImageSearchEngine is distributed under a BSD-style license.
 
It's an image similarity search Engine built on top of Lire.  The images and the product web page of it's  can be filtered using a query by keywords [support Chinese]and  are afterwards optically ranked. This engine provides an easy to use REST  interface and returns the search results as XML.


If you're familiar spring RESTTempleate you can refer my test case in the code.

#Useage

Set index file path:

```Java
IndexConstant {
        public static final String INDEX_DATA_FILE_PATH = "c:\\data";//Modify this path
        
```

## Index Image

```Java
@ContextConfiguration("classpath:app-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class RestIndexImageTest {
    @Autowired
	RestTemplate rt;
	@Test
	public void testFileUpload() {

		String url = "http://localhost:8081/ImageSearchEngine/indexProductImage";

		Resource resource = new ClassPathResource("image/psbe.jpg");

		MultiValueMap<String, Object> mvm = new LinkedMultiValueMap<String, Object>();
		mvm.add("productUrl", "http://www.importnew.com/7774.html");
		mvm.add("imageUrl", "");
		mvm.add("imagefile", resource);
		mvm.add("keywords", "test 测试");
		ResponseEntity<String> respEnt = rt.postForEntity(url, mvm, String.class);
		System.out.println("respEnt"+respEnt.getBody());
	}
}
```

## Search Image by keyword and image file

```java
@ContextConfiguration("classpath:app-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class RestSearchImageTest {
    @Autowired
	RestTemplate rt;
	@Test
	public void testSearchProductImage() {

		String url = "http://localhost:8081/ImageSearchEngine/searchProductImage";

		Resource resource = new ClassPathResource("image/psbe.jpg");

		MultiValueMap<String, Object> mvm = new LinkedMultiValueMap<String, Object>();

		mvm.add("imageUrl", "");
		mvm.add("imagefile", resource);
		mvm.add("keywords", "测试");
		ResponseEntity<String> respEnt = rt.postForEntity(url, mvm, String.class);
		System.out.println("respEnt:" + respEnt.getBody());
	}
}
```
