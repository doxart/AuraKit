Aşağıdaki adımları sırasıyla uygulayarak "C:\Projects\MobileMPSources\easywallet" dizinindeki belirtilen bileşenleri incele, izole et ve AuraKit kütüphanesine bağımsız, esnek ve modüler Compose Multiplatform bileşenleri olarak entegre et:

#### 1. TopAppBar Entegrasyonu (`AppBar.kt` & `ViewController.kt`)
* **Kaynak:** `AppBar.kt` içindeki `TopAppBar` mantığı ve `ViewController.kt` içindeki çağırma/eylem deseni.
* **Gereksinimler:**
  - Bileşeni AuraKit standartlarına uygun, kompakt ve yeniden kullanılabilir bir API ile tasarla.
  - `ViewController.kt` içinde action alanında kullanılan `Surface`, icon wrapper veya chip benzeri konteyner yapılarını `actions` slot'u veya hazır action tipleriyle entegre ve esnek hale getir.
  - Sabit/hardcoded bağımlılıkları temizle; başlık, navigasyon ikonu, aksiyon slotu ve arka plan stilini parametrik yap.

#### 2. Chart Bileşenlerinin İzolasyonu (`Insights.kt`)
* **Kaynak:** `MonthlyStatsView` içinde yer alan Bar Chart ve Line Chart çizim mantıkları.
* **Gereksinimler:**
  - `MonthlyStatsView` kart sarmalayıcısını (card wrapper) tamamen hariç tut; yalnızca saf çizim (Canvas/chart) mantıklarını çek.
  - AuraKit içine `AuraBarChart` ve `AuraLineChart` olarak iki bağımsız bileşen şeklinde modülerleştir.
  - Veri modellerini generic veya gevşek bağlı (loosely-coupled) bir data class (`List<BarData>`, `List<PointData>`) üzerinden parametre olarak alacak şekilde soyutla.

#### 3. Ana Modal ve Dinamik Input Dialog Sistemi (`Pops.kt` & `InputCreator.kt`)
* **Kaynak:** `Pops.kt` içindeki `MainPop` ve `InputCreator.kt` içindeki `InputPop` yapıları.
* **Gereksinimler:**
  - **AuraModalDialog (`MainPop`):** Sistemin ana diyalog/bottom-sheet taşıyıcısı olacak şekilde generic bir `content: @Composable () -> Unit` slotu ile yeniden yaz. İçerisine herhangi bir composable verilip render edilebilmeli; dismiss davranışları ve animasyonları izole olmalı.
  - **AuraInputDialog (`InputPop`):** `MainPop` altyapısını kullanan hazır bir giriş diyaloğu haline getir.
  - **Input Tiplerini Genişlet:** Mevcutta sadece para birimi (currency) destekleyen yapıyı genişleterek bir enum/sealed class ile şu modları destekleyecek kompakt bir API oluştur:
    - `Currency` (para formatlama/maskeleme ile)
    - `Integer` (düz tam sayı)
    - `Decimal` (ondalıklı sayı)
    - `Phone` (telefon numarası)
    - `Code / OTP` (doğrulama/kod formatı)
    - `Password` (gizli/maskeli metin)
  - Her tip için uygun `KeyboardType`, `VisualTransformation` ve doğrulama (validation) kurallarını otomatik eşle.

Tüm kodları harici bağımlılıklardan arındırılmış, Compose Multiplatform (Android & iOS) uyumlu ve AuraKit paket hiyerarşisine uygun olacak şekilde üret.

Tüm bunları bitirdiğinde tekrar kullanılabilirlik açısından AuraKit ana sayfada bu yönergeleri tekrar kullanabilmek açısından kendin için bir yönerge hazırla. Daha sonra bu yönergeyi kullanarak yeni özellikler ekleyebiliriz. 


Kullanıcı, kolayca yeni özellikler eklemek istediğinde bu dosyayı kullanarak, kolayca yeni yönergeler hazırlayıp, aynı adımları izleyerek, bu özelliklerin entegrasyonunu sağlayabilir.

