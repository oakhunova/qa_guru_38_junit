import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class LiveJournalParameterizedTests {

    @BeforeAll
    static void pageLoad() {
        Configuration.pageLoadStrategy = "eager";
    }

    @BeforeEach
    void setUp() {
        Configuration.timeout = 10000;
        Configuration.browserSize = "1920x1080";
        open("https://www.livejournal.com/");
    }

    @AfterEach
    void tearDown() {
        Selenide.closeWebDriver();
    }

    @ParameterizedTest
    @CsvSource({
            "Новые лица, Новые лица",
            "фотография, ФОТОГРАФИЯ",
            "Еда, ЕДА",
            "Кино, КИНО"
    })
    void testMainNavigationMenu(String menuItem, String expectedTitle) {
        $$(".categories__link.categories__link--topcategory").findBy(text(menuItem)).click();
        $(".body-content").shouldBe(visible);
        $(".mainpage__title").shouldHave(text(expectedTitle));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "English", "Deutsch", "Français", "Español"
    })
    void testLanguageSwitcher(String expectedLanguage) {
        $(".s-header-item__link--lang").click();
        $$(".s-header-sub-list-item__link").findBy(text(expectedLanguage)).click();
        $(".s-header").shouldHave(text(expectedLanguage));
    }

    @ParameterizedTest
    @MethodSource(value = "searchQueriesProvider")
    void searchForDifferentTopics(String searchQuery) {
    $(".s-header-search__btn").click();
    $(".s-header-search__input").shouldBe(visible)
            .setValue(searchQuery)
            .pressEnter();
    switchTo().window(1);
    $$(".rsearch-note").shouldHave(sizeGreaterThan(0));
}

    static Stream<String> searchQueriesProvider() {
        return Stream.of("программирование", "скейтборд", "музыка");
    }
}
