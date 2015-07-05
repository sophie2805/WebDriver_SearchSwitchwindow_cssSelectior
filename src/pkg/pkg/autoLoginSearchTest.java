package pkg;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by Sophie on 15/7/4.
 */
public class autoLoginSearchTest {
    private WebDriver dr;
    private String url;

    @Before
    public void setup() throws Exception {
        dr = new FirefoxDriver();
        url = new String("https://www.wacai.com/user/user.action?url=http%3A%2F%2Fbbs.wacai.com%2Fportal.php");
        dr.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test
    public void actionTest() throws Exception {
        //登录
        dr.get(url);
        dr.findElement(By.id("account")).clear();
        dr.findElement(By.id("account")).sendKeys("******");
        dr.findElement(By.id("pwd")).clear();
        dr.findElement(By.id("pwd")).sendKeys("******");
        dr.findElement(By.id("login-btn")).click();
        //打开理财规划子版块，然后关闭左侧导航栏
        dr.findElement(By.linkText("理财规划")).click();
        //用WindowHandle的方式获取新开页面
        String currentWindow = dr.getWindowHandle();
        Set<String> handles = dr.getWindowHandles();
        Iterator h = handles.iterator();
        while (h.hasNext()) {
            dr = dr.switchTo().window((String) h.next());
        }
        System.out.println(dr.getCurrentUrl());
        dr.manage().window().maximize();
        dr.findElement(By.cssSelector("a.comiis_left_closes")).click();

        //点开搜索类型下拉框，将鼠标移动到用户上并选择
        Actions act = new Actions(dr);
        WebElement dropDown = dr.findElement(By.cssSelector("a.showmenu.xg1"));
        WebElement user = dr.findElement(By.cssSelector("ul#comiis_twtsc_type_menu>li>a[rel='user']"));
        act.click(dropDown).perform();
        act.moveToElement(user).click().perform();
        act.moveByOffset(20, 30).click().perform();
        //为了把过程看的更仔细，所以在此休眠1s
        Thread.sleep(1000);
        //找到搜索输入框，并输入关键字，然后点击搜索按钮
        WebElement input = dr.findElement(By.id("comiis_twtsc_txt"));
        input.clear();
        input.sendKeys("周杰伦");
        dr.findElement(By.id("comiis_twtsc_btn")).click();//Click之后FireFox新开了一个页面

        //用WindowHandle+页面title来切换dr至我们想要的窗口
        currentWindow = dr.getWindowHandle();
        handles = dr.getWindowHandles();
        //System.out.println(handles.size());
        h = handles.iterator();
        while (h.hasNext()) {
            dr = dr.switchTo().window((String) h.next());
            //System.out.println(dr.getTitle());
            if (dr.getTitle().equals("查找好友 - 挖财社区"))
                break;
        }
        //xpath模糊查找
        List<WebElement> searchResult = dr.findElements(By.xpath("//a[contains(@title,'周杰伦')]"));
        System.out.println("搜索到" + searchResult.size() + "个周杰伦粉丝，他们分别是：");
        for (int i = 0; i < searchResult.size(); i++) {
            System.out.println(searchResult.get(i).getAttribute("title"));
        }

    }

    @After
    public void tearDown() throws Exception {
        dr.quit();
    }
}
