package com.example.telematyka

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        val weekTitle = findViewById<TextView>(R.id.WeekNumber)
        val weekDescription = findViewById<TextView>(R.id.WeekDescription)
        val babyImage = findViewById<ImageView>(R.id.babyImageMain)

        // Pobieramy tydzień przesłany z MainActivity (domyślnie 1, jeśli coś pójdzie nie tak)
        val week = intent.getIntExtra("WEEK_KEY", 1)

        weekTitle.text = "Tydzień $week"

        // Ustawienie tekstu edukacyjnego w zależności od tygodnia
        weekDescription.text = when (week) {
            5 -> "Zarodek przybiera podłużny kształt. Tworzy się cewa nerwowa, z której rozwinie się układ nerwowy. Pojawia się kosmówka i zaczyna być produkowany płyn owodniowy. Tworzą się zawiązki kończyn oraz zalążki: serca, jelit, przysadki mózgowej, ośrodkowego układu nerwowego."
            6 -> "To dobry moment na wykonanie pierwszego USG, podczas którego będzie można zaobserwować czynność skurczową serca. W nowo powstałym organizmie zaczyna krążyć krew. Nadal rozwijają się układy: szkieletowy, mięśniowy, pokarmowy, oddechowy, moczowy, płciowy, wydalniczy. Pojawiają się zawiązki oczu. Zaczyna się kształtować trzustka i wątroba."
            7 -> "Rozwój mózgu w piątym tygodniu rozwoju zarodka jest bardzo intensywny. Powstają oskrzela i kształtują się nadnercza. Zaczyna tworzyć się narząd gardłowy, z którego następnie powstanie żuchwa, podniebienie i język. Kształtują się usta i nos."
            8 -> "W szóstym tygodniu powstają płytki ręki i stopy. Wytwarza się fizjologiczna przepuklina pępkowa. Zaczyna się produkcja moczu przez śródnercza."
            9 -> "W siódmym tygodniu uformowany jest pień mózgowia. Zaczynają powstawać kubki smakowe oraz nabłonek węchowy. Ostatecznie kształtuje się tarczyca i osiąga swoje docelowe położenie. Tworzą się zawiązki palców. Pojawiają się łokcie."
            10 -> "W ósmym tygodniu twarzoczaszka zarodka jest w pełni ukształtowana i zaczyna przypominać twarz noworodka. Formują się powieki, górna warga i nos. Zaczynają rozwijać się uszy. Skórka jest cienka i przezroczysta. Tułów i szyja zaczynają się prostować. Następuje rozwój kręgosłupa i układu kostnego. Zarodek zaczyna się poruszać, jednak nie jest to odczuwane przez matkę."
            11 -> "Okres płodowy rozpoczyna się z pierwszym dniem dziewiątego tygodnia ciąży. Wątroba podejmuje funkcję hematopoezy, pojawiają się punkty kostnienia w kościach czaszki i kończyn. Pojawiają się ruchy oddechowe. Płód otwiera buzię i połyka."
            12 -> "W 10. tygodniu rozwoju płodu można wysłuchać tętna płodu. Formują się odciski palców. struktury mózgu dziecka są już ukształtowane, a jego układ pokarmowy staje się drożny, ponieważ wykształcił się już odbyt."
            13 -> "W tej fazie rozwoju płód intensywnie się porusza. Pojawiają się również ruchy oddechowe. Przy odrobinie szczęścia w 11. tygodniu rozwoju płodu, a więc w 13. tygodniu ciąży, podczas badania USG można już określić płeć dziecka. Zazwyczaj na tym etapie jelita dziecka zajmują docelowe miejsce. Znika fizjologiczna przepuklina pępkowa. W 11. tygodniu rozwoju płodu zaczynają powstawać struny głosowe. Maluch intensywnie połyka wody płodowe i czuje ich smak, ponieważ ma rozwinięte kubki smakowe."
            14 -> "W 12. tygodniu życia płodowego zaczyna się produkcja żółci w wątrobie. Tarczyca magazynuje jod i wytwarza hormony. Przysadka również osiąga zdolność do syntezy wszystkich hormonów. Trzustka produkuje insulinę, a nerki wytwarzają mocz. Można zobaczyć dłonie i stopy oraz policzyć palce (również paznokcie palców stóp mogą być widoczne). Maluch jest bardzo ruchliwy. Zmienia pozycję, przeciąga się, a jego tkanka mięśniowa jest coraz mocniejsza."
            15 -> "Maluch potrafi połykać oraz ssać i stale ćwiczy te umiejętności. W 13. tygodniu swojego rozwoju zaczyna słyszeć. Dochodzące do niego dźwięki to przede wszystkim bicie serca matki. Maluch zaczyna także reagować na bodźce z zewnątrz, dlatego zasadne wydaje się głaskanie brzucha matki. Dziecko jest nadal bardzo ruchliwe."
            16 -> "W 14. tygodniu życia płodowego dziecko wykonuje ruchy gałek ocznych i reaguje na światło, choć jego powieki są nadal zamknięte. Dziecko mocno się prostuje, a jego mięśnie są coraz silniejsze. W mózgowiu płodu można rozróżnić poszczególne płaty. Postępuje kształtowanie słuchu. Skórę głowy zaczynają pokrywać włoski (meszek płodowy)."
            17 -> "Postępuje rozwój układu nerwowego, dzięki czemu płód wykonuje złożone ruchy kończynami, połyka, ziewa, ćwiczy odruch ssania. Płuca również podlegają dalszym zmianom."
            18 -> "W 18. tygodniu ciąży, a 16. tygodniu rozwoju płodu zaczynają być odczuwalne ruchy malucha, który: fika koziołki, obraca się, kopie, czka. Ważnym momentem w rozwoju płodu, do którego dochodzi na tym etapie, jest powstanie osłonki mielinowej wokół włókien nerwowych. Jest ona niezbędna do prawidłowego funkcjonowania mózgu. W USG można zobrazować kompletny robak móżdżku. Płód ma długość 12-14 cm, waży 100-150 gramów. U płodów żeńskich pojawiają się jajniki. Widoczne są uszy. Płód zaczyna odbierać dźwięk dochodzący z zewnątrz."
            19 -> "Na tym etapie dziecko jest pokryte mazią płodową, dzięki której po urodzeniu nie będzie tak pomarszczone. W 17. tygodniu życia płodowego normalnie funkcjonuje już większość układów i narządów wewnętrznych. U chłopców jądra zaczynają produkować testosteron."
            20 -> "W 18. tygodniu rozwoju płodu nadal dużo się dzieje. U dziewczynek macica jest w pełni ukształtowana, a w jajnikach znajdują się komórki jajowe. Zaczyna się także tworzyć kanał pochwowy. U chłopców rozpoczyna się zstępowanie jąder z jamy brzusznej. Matka może już odczuwać ruchy dziecka, a dziecko rozpoznaje głos matki. Płód jest pokryty meszkiem płodowym."
            21 -> "Dziecko, połykając wody płodowe, kształtuje swoje preferencje smakowe. Na smak wód ma wpływ to, co zje matka. 19. tydzień rozwoju płodu to także ważny moment w rozwoju mózgu. Tworzą się połączenia między neuronami, co pozwoli mózgowi dziecka wysyłać odpowiednie sygnały do ciała. Płód zaczyna chwytać, a mama może odczuwać napady czkawki u dziecka. W tym czasie rusza także produkcja białych krwinek i rozwija się układ odpornościowy."
            22 -> "Dziecko potrafi już odróżniać światło od mroku. Długość płodu wynosi około 19 cm, jego waga to 300-350 gramów. Jest on pokryty meszkiem włosowym oraz mazią płodową. Spędza czas na trenowaniu umiejętności uścisku dłoni, m.in. chwytając pępowinę. Małżowiny uszne dziecka mają już swój unikalny kształt, a mimika jego twarzy jest coraz bogatsza – potrafi np. marszczyć brwi. Jego wątroba zaczyna rozkładać bilirubinę."
            23 -> "Dziecko waży ok. 580 gramów, może mieć 30 cm długości. Wykonuje gwałtowne ruchy gałek ocznych podczas snu."
            24 -> "Maluchowi robi się w brzuchu mamy coraz ciaśniej. 22. tydzień rozwoju płodu to czas, kiedy płuca dziecka rozpoczynają produkcję surfaktantu – substancji, która zmniejsza napięcie w pęcherzykach płucnych. Na tym etapie ruchy gałek ocznych są już bardziej skoordynowane – choć powieki malucha są nadal zamknięte, to patrzy on w górę, w dół i na boki."
            25 -> "Najważniejsze zmiany w 23. tygodniu rozwoju płodu to: dynamiczny rozwój kory mózgowej (mózg jest coraz bardziej pofalowany), otwarcie się nozdrzy dziecka, tworzenie się naczyń włosowatych, które napełnią się krwią, przez co ciało płodu stanie się różowe, nabycie umiejętności zaciskania dłoni, nabycie możliwości oddychania pod koniec 23. tygodnia."
            26 -> "Dziecko waży już ok. 600-800 gramów, jest pokryte cienką warstwą skóry i zaczyna odbierać bodźce słuchowe. Ssie kciuk, co przy odrobinie szczęścia można zaobserwować podczas badania USG."
            27 -> "Siatkówka oka jest już w pełni rozwinięta. Mięśnie dziecka kurczą się i rozkurczają. Szybko przybiera ono na wadze – może pić nawet pół litra płynu owodniowego dziennie! Płód stale przygotowuje się do życia poza ciałem matki – z każdym dniem coraz sprawniej funkcjonuje jego układ oddechowy, pokarmowy i krwionośny. Postępuje także rozwój układu nerwowego."
            28 -> "Na tym etapie rozwoju płód zaczyna mrugać i ziewać. Ma rzęsy i brwi, a jego skóra jest czerwona. Dziecko w 26. tygodniu swojego rozwoju śni, o czym świadczy szybki ruch gałek ocznych, który można zaobserwować podczas badania USG 4D."
            29 -> "Dziecko jest coraz większe, coraz trudniej mu wykonywać swobodne ruchy, więc rozpycha się w brzuchu mamy. 27. tydzień to czas, kiedy postępuje rozwój mózgu płodu i tworzą się nowe połączenia między komórkami nerwowymi. Maluch, choć bardzo dobrze chroniony, teoretycznie może już odczuwać ból. Rozwinięty jest już zmysł węchu. Pod skórą dziecka gromadzi się coraz więcej tkanki tłuszczowej."
            30 -> "W 28. tygodniu życia płodu trwa intensywny rozwój jego mózgu: kora mózgowa jest dojrzała, postępuje mielinizacja mózgu, rozwijają się obszary mózgu odpowiedzialne za narządy zmysłów. Dziecko otwiera oczy, które są wrażliwe na światło. Ze skóry płodu powoli znika charakterystyczny meszek. Znacznie rozrasta się tkanka podskórna."
            31 -> "Na tym etapie rozwoju maluch uśmiecha się, niepokoi – ma temperament, który najpewniej nie zmieni się po przyjściu na świat. Następuje dalszy rozwój mózgu dziecka, który potrafi już przetwarzać informacje. Wszystko dzięki powstałej właśnie sieci połączeń między neuronami mózgu. W trzecim trymestrze ciąży mózg rozwija się najintensywniej, dlatego bardzo ważne jest to, jakich składników dostarcza mu matka. W jej diecie nie powinno zabraknąć kwasów omega-3."
            32 -> "Poród coraz bliżej, więc w brzuchu matki trwają przygotowania. Płód: może już przyjąć pozycję główkową, ćwiczy picie, siusianie, wykonuje ruchy oddechowe, przybiera na wadze."
            33 -> "Jakie zmiany zachodzą w 31. tygodniu rozwoju płodu? Kości dziecka stają się już całkiem twarde (poza kośćmi czaszki). Następuje dalszy rozwój płuc, co umożliwi dziecku samodzielne funkcjonowanie. W mózgu rozwija się ośrodek Broki, który w przyszłości będzie odpowiadać za mowę dziecka."
            34 -> "W 32. tygodniu życia płodu rozwijają się jeszcze układy: oddechowy i płciowy. Pozostałe są już w pełni ukształtowane, podobnie jak organy wewnętrzne. W przypadku chłopców jądra powinny już być w mosznie. Paznokcie dziecka sięgają już czubków palców. Ruchy dziecka mogą stać się bolesne dla matki."
            35 -> "Na tym etapie trwa dalszy rozwój mózgu – przybywa komórek mózgowych. Poza tym dziecko nadal doskonali wszystkie swoje umiejętności."
            36 -> "W tym tygodniu następuje ostatni etap dojrzewania płuc. Maluch wędruje w dół kanału rodnego. Jeśli na tym etapie nieoczekiwanie dojdzie do porodu, maluch prawdopodobnie będzie już w stanie funkcjonować samodzielnie."
            37 -> "Dziecko jest już gotowe do życia poza ciałem matki. Jego organizm zaczyna wytwarzać kortyzol – hormon, dzięki któremu możliwe będzie samodzielne oddychanie."
            38 -> "Dziecko w każdej chwili może pojawić się na świecie. Niecierpliwy maluch napiera na kanał rodny. Waga łożyska jest taka sama jak dziecka."
            39 -> "Główka dziecka jest coraz niżej. Trwa intensywny rozwój mózgu, który będzie dokonywał się także w pierwszych latach życia malucha."
            40 -> "38. tydzień rozwoju płodu to zarazem 40. tydzień ciąży, a więc moment rozwiązania."
            else -> "To piękny czas rozwoju Twojego maleństwa. Pamiętaj o regularnych badaniach i zdrowej diecie!"
        }

        // Ustawienie zdjęcia (miesiąca) w zależności od tygodnia ciąży
        val imageResource = when (week) {
            in 1..5 -> R.drawable.telematyka_1month_clear
            in 6..9 -> R.drawable.telematyka_2month_clear
            in 10..13 -> R.drawable.telematyka_3month_clear
            in 14..17 -> R.drawable.telematyka_4month_clear
            in 18..22 -> R.drawable.telematyka_5month_clear
            in 23..27 -> R.drawable.telematyka_6month_clear
            in 28..31 -> R.drawable.telematyka_7month_clear
            in 32..35 -> R.drawable.telematyka_8month_clear
            in 36..42 -> R.drawable.telematyka_9month_clear
            else -> R.drawable.telematyka_1month_clear // Domyślne zdjęcie w przypadku anomalii
        }

        babyImage.setImageResource(imageResource)
    }
}