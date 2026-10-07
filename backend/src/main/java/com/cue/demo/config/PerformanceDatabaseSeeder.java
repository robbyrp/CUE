package com.cue.demo.config;

import com.cue.demo.entities.Credit;
import com.cue.demo.entities.Performance;
import com.cue.demo.repositories.PerformanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Component @Profile("spectacol_database_seeder")
public class PerformanceDatabaseSeeder implements CommandLineRunner {
    private final PerformanceRepository performanceRepository;

    @Override
    public void run(String... args) {
        final String theaterConstanta = "Teatrul de Stat Constanta";
        final String tnb = "TNB";

        String minuneaMinunilorUrl = "https://images.unsplash.com/photo-1503095396549-807759245b35";
        Performance minuneaMinunilor = Performance.builder()
                .title("Minunea minunilor")
                .director("DANIEL CHIRILĂ")
                .location("Scena Deschisă")
                .theaterName(theaterConstanta)
                .startDateTime(LocalDateTime.of(2026, 7, 11, 19, 30))
                .coverImageURL(getCoverImageUrl(minuneaMinunilorUrl))
                .ageLimit(14)
                .duration(60)
                .fullCoverImageURL(getFullCoverImageUrl(minuneaMinunilorUrl))
                .purchaseTicketLink("https://www.teatruldestatconstanta.ro/spectacol/minunea-minunilor")
                .description("Adunată în satul Vânători pentru o zi de pomenire și aniversare, familia Gămănuț își împletește cele trei generații printre amintiri vii, certuri, râsete și vechi legende strămoșești. Ritmul acestei reuniuni este însă rupt brusc când la poartă sosește un colet misterios: o cutie ruginită expediată bătrânului Nică. O scrisoare uitată și câteva obiecte dinăuntru scot la lumină un secret îngropat adânc, forțându-i pe toți să rescrie din temelii adevărul despre propriul lor trecut.")
                .creditList(List.of(
                        new Credit("Scenografia", List.of("Lăcrămioara Dumitrașcu")),
                        new Credit("Regia tehnica:", List.of("Tudor Cioboteanu"))
                ))
                .build();

        String orlandoUrl = "https://images.unsplash.com/photo-1632237926612-7064eac88357";
        Performance orlando = Performance.builder()
                .title("Orlando")
                .director("Florin Caracala")
                .location("Scena Deschisă")
                .coverImageURL(getCoverImageUrl(orlandoUrl))
                .theaterName(theaterConstanta)
                .startDateTime(LocalDateTime.of(2026, 5, 28, 20, 30))
                .ageLimit(14)
                .duration(165)
                .fullCoverImageURL(getFullCoverImageUrl(orlandoUrl))
                .purchaseTicketLink("https://www.teatruldestatconstanta.ro/spectacol/orlando")
                .description("Pornind din splendoarea epocii elisabetane, nobilul și neliniștitul Orlando traversează secole de pasiuni și convenții sociale, până când o transformare misterioasă îi schimbă complet trupul și genul. Rămasă aceeași conștiință într-o lume guvernată brusc de alte rigori și interdicții, eroina înfruntă iluzia identității și prețul libertății de a fi. Viziunea scenică a lui Sarah Ruhl reinterpretează capodopera Virginiei Woolf ca pe un joc poetic vibrant, o odă adusă căutării de sine dincolo de măștile timpului și ale societății.")
                .creditList(List.of(
                        new Credit("Coregrafia", List.of("George Pleșca")),
                        new Credit("Scenografia", List.of("Ana Ienașcu")),
                        new Credit("Lighting Design", List.of("Lucian Prasti"))
                ))
                .build();


        String livadaUrl = "https://plus.unsplash.com/premium_photo-1695717374112-6257c3a5eeef";
        Performance livada = Performance.builder()
                .title("LIVADA DE VIȘINI")
                .director("Raluca Rădulescu")
                .location("Sala Mare")
                .coverImageURL(getCoverImageUrl(livadaUrl))
                .theaterName(theaterConstanta)
                .startDateTime(LocalDateTime.of(2025, 10, 11, 17, 0))
                .ageLimit(14)
                .duration(150)
                .fullCoverImageURL(getFullCoverImageUrl(livadaUrl))
                .purchaseTicketLink("https://www.teatruldestatconstanta.ro/spectacol/livada-de-visini-1")
                .description("Revenită din străinătate la vechiul conac natal alături de fiica sa, Ania, aristocrata ruinată Liubov Ranevskaia se agață cu disperare de o lume a privilegiilor pe care refuză să o lase să piară. În timp ce Rusia începutului de secol XX fierbe sub presiunea schimbărilor sociale, sacrificarea legendarei livezi de vișini devine singura cale de scăpare din capcana datoriilor și epilogul dureros al unei întregi clase condamnate la dispariție.")
                .creditList(List.of(
                        new Credit("Coregrafia", List.of("Andreea Gavriliu")),
                        new Credit("Sound design", List.of("Claudiu Urse")),
                        new Credit("Lighting Design", List.of("Sabina Reus"))
                ))
                .build();

        String incendiiUrl = "https://plus.unsplash.com/premium_photo-1716078137428-aabec80b2c34";
        Performance incendii = Performance.builder()
                .title("Incendii")
                .director("Alexandru Mâzgăreanu")
                .location("Sala Studio")
                .coverImageURL(getCoverImageUrl(incendiiUrl))
                .theaterName(theaterConstanta)
                .startDateTime(LocalDateTime.of(2024, 12, 20, 18, 0))
                .ageLimit(16)
                .duration(150)
                .fullCoverImageURL(getFullCoverImageUrl(incendiiUrl))
                .purchaseTicketLink("https://www.teatruldestatconstanta.ro/spectacol/incendii")
                .description("Împinși de testamentul enigmatic al mamei lor abia trecute în neființă, doi gemeni părăsesc siguranța Occidentului pentru a pătrunde în ruinele unui Orient Mijlociu sfâșiat de război, pe urmele unui tată și ale unui frate de a căror existență nu știuseră nimic. Această coborâre inițiatică devine o confruntare viscerală cu ororile conflictului și rănile trecutului, explorând granița fragilă dintre bestialitatea violenței și suferința umană preschimbată în tăcere.")
                .creditList(List.of(
                        new Credit("Decor", List.of("Andreea Săndulescu")),
                        new Credit("Costume", List.of("Alexandra Boerescu")),
                        new Credit("Muzica", List.of("Alexandru Suciu"))
                ))
                .build();


        String casaUrl = "https://images.unsplash.com/photo-1618717863045-6c02cbe51e48";
        Performance casaBernardei = Performance.builder()
                .title("CASA BERNARDEI ALBA")
                .director("Diana Csiki-Mititelu")
                .location("Sala Studio")
                .coverImageURL(getCoverImageUrl(casaUrl))
                .theaterName(theaterConstanta)
                .startDateTime(LocalDateTime.of(2026, 4, 26, 19, 0))
                .ageLimit(15)
                .duration(120)
                .fullCoverImageURL(getFullCoverImageUrl(casaUrl))
                .purchaseTicketLink("https://www.teatruldestatconstanta.ro/spectacol/casa-bernardei-alba")
                .description("Într-o casă andaluză transformată într-o fortăreață a doliului absolut, tirania Bernardei Alba strivește fără milă destinele celor opt femei zăvorâte între zidurile sale în numele reputației și al dogmelor patriarhale. Căsătoria din interes plănuită pentru cea mai vârstnică dintre surori nu face decât să ațâțe pasiunile interzise și setea disperată de libertate a celorlalte fiice. Capodoperă a „trilogiei rurale” a lui Lorca, textul surprinde coliziunea violentă dintre natura umană nestăpânită și zidurile impenetrabile ale unei lumi care pedepsește iubirea cu moartea.")
                .creditList(List.of(
                        new Credit("Scenografia", List.of("Alexandra Budianu")),
                        new Credit("Sound design", List.of("Diana Csiki-Mititelu")),
                        new Credit("Lighting Design", List.of("Cristian Niculescu"))
                ))
                .build();

        String logodnaUrl = "https://plus.unsplash.com/premium_photo-1684923604471-8ed294f08b66";
        Performance logodna = Performance.builder()
                .title("LOGODNĂ RELATIVĂ")
                .director("Iulian Enache")
                .location("Sala Nouă")
                .coverImageURL(getCoverImageUrl(logodnaUrl))
                .theaterName(theaterConstanta)
                .startDateTime(LocalDateTime.of(2023, 2, 4, 16, 0))
                .ageLimit(6)
                .duration(120)
                .fullCoverImageURL(getFullCoverImageUrl(logodnaUrl))
                .purchaseTicketLink("https://www.teatruldestatconstanta.ro/spectacol/logodna-relativa")
                .description("Patru personaje prinse într-un carusel de aparențe și două cupluri care își pasează secretele dintr-o neînțelegere în alta dau tonul unei comedii savuroase despre fragilitatea fidelității. În „Logodnă relativă”, farsa conjugală a dramaturgului Alan Ayckbourn împinge absurdul la limită prin quiproquo-uri ingenioase, transformând infidelitățile și suspiciunile într-o poveste plină de farmec și lejeritate. Textul rămâne unul dintre cele mai aplaudate succese britanice tocmai prin felul strălucit în care demască ipocrizia relațiilor printr-un umor inteligent, cald și mereu surprinzător.")
                .creditList(List.of(
                        new Credit("Scenografia", List.of("Gabi Albu")),
                        new Credit("Asistent scenografie", List.of("Ruxandra Bobleagă")),
                        new Credit("Lighting Design", List.of("Alexandru Bibere"))
                ))
                .build();

        String jocuriUrl = "https://images.unsplash.com/photo-1673854278809-4fe0836b876b";
        Performance jocuri = Performance.builder()
                .title("JOCURI ÎN CURTEA DIN SPATE")
                .director("Edna Mazya")
                .location("Sala Studio")
                .coverImageURL(getCoverImageUrl(jocuriUrl))
                .theaterName(theaterConstanta)
                .startDateTime(LocalDateTime.of(2020, 2, 29, 19, 45))
                .ageLimit(14)
                .duration(70)
                .fullCoverImageURL(getFullCoverImageUrl(jocuriUrl))
                .purchaseTicketLink("https://www.teatruldestatconstanta.ro/spectacol/jocuri-in-curtea-din-spate")
                .description("Dincolo de reconstituirea unei fapte crude din universul adolescenței, textul Ednei Mazya sondează mecanismele psihologice toxice care au făcut posibilă o astfel de violență. Patru băieți și o fată își consumă într-o aparentă banalitate ultima zi de inocență și libertate, înainte ca granița fragilă dintre joacă și agresiune să se prăbușească definitiv. Inspirată dintr-un caz real petrecut într-o comunitate din nordul Israelului, piesa a stârnit un ecou internațional răsunător, transformând o dramă sfâșietoare într-un manifest curajos împotriva tăcerii și a complicității sociale.")
                .creditList(List.of(
                        new Credit("Regia artistică și ilustrația muzicală", List.of("Diana Csiki-Mititelu")),
                        new Credit("Scenografia", List.of("Răzvan Bordoș")),
                        new Credit("Lighting Design", List.of("Cristian Niculescu"))
                ))
                .build();

        String indoialaUrl = "https://images.unsplash.com/photo-1630352225273-e4ddb7337f34";
        Performance indoiala = Performance.builder()
                .title("Îndoiala")
                .director("Diana Csiki-Mititelu")
                .location("Sala Studio")
                .coverImageURL(getCoverImageUrl(indoialaUrl))
                .theaterName(theaterConstanta)
                .startDateTime(LocalDateTime.of(2022, 4, 16, 14, 0))
                .ageLimit(6)
                .duration(95)
                .fullCoverImageURL(getFullCoverImageUrl(indoialaUrl))
                .purchaseTicketLink("https://www.teatruldestatconstanta.ro/spectacol/indoiala")
                .description("„Îndoiala poate fi o legătură la fel de puternică și de eliberatoare ca certitudinea”, avertizează Părintele Flynn încă din deschiderea unei confruntări morale nemiloase. În incinta unei școli catolice newyorkeze, rigida directoare Aloysius pornește o cruciadă tăcută împotriva carismaticului preot, suspectat de abuz în urma observațiilor unei tinere călugărițe. Laureată cu Tony și Pulitzer și transpusă într-un celebru film de Oscar, capodopera lui John Patrick Shanley refuză verdictul facil, transformând ambiguitatea într-o disecție tulburătoare a conștiinței, a dogmei și a fragilității adevărului.")
                .creditList(List.of(
                        new Credit("Regia", List.of("Diana Csiki-Mititelu")),
                        new Credit("Decor", List.of("Andreea Săndulescu")),
                        new Credit("Costume și asistent decor", List.of("Ioana Ungureanu")),
                        new Credit("Ilustrația muzicală", List.of("Adrian Piciorea"))
                ))
                .build();

        String amintiriUrl = "https://images.unsplash.com/photo-1759409972484-cdffb90d1910";
        Performance amintiri = Performance.builder()
                .title("Amintiri din copilărie")
                .director("Vitalie Bichir")
                .location("Sala Atelier")
                .theaterName(tnb)
                .startDateTime(LocalDateTime.of(2026, 12, 5, 19, 0))
                .coverImageURL(getCoverImageUrl(amintiriUrl))
                .ageLimit(6)
                .duration(75)
                .fullCoverImageURL(getFullCoverImageUrl(amintiriUrl))
                .purchaseTicketLink("https://www.tnb.ro/ro/amintiri-din-copilarie")
                .description("Spectacolul s-a născut dintr-o călătorie făcută în vara lui 2023 în satul natal, Brînza din Republica Moldova, unde autorul a mers să-și rezolve o problemă birocratică legată de certificatul de naștere. Situația avea la bază o particularitate sensibilă: în actele românești, bunicii săi (Ilie și Chirilă) figurează drept părinți, transformându-l într-un caz unic de cetățean român cu părinți de același sex.\n" +
                        "\n" +
                        "Ajuns la casa părintească, găsită într-o stare de abandon, a avut impresia că însăși locuința îi vorbește, asemenea fântânii din povestea lui Creangă. Așezat pe una dintre buturugile lăsate în urmă de mama sa, a început să citească Amintiri din copilărie. Lectura s-a transformat rapid într-o oglindă a propriei sale istorii: episoadele clasice s-au suprapust peste amintirile lui din infancia petrecută la sat – cireșele au devenit lalele, mătușa Mărioara a fost înlocuită de mătușa Ana, moș Vasile a devenit moș Ion, iar vărul Ion a luat chipul vărului Eugen, plecat astăzi în Italia.\n" +
                        "\n" +
                        "Din această adunare organică de amintiri s-a conturat spectacolul însuși. Fără nevoia unui regizor extern, pentru că materialul uman și emoțional venea dintr-o sinceritate absolută, demersul nu își propune să expună o virtuozitate actoricească, ci să ofere publicului un spațiu comun de regăsire a propriei copilării.")
                .creditList(List.of(
                        new Credit("Regia", List.of("Vitalie Bichir")),
                        new Credit("Lumini", List.of("Vasile Neguț", "Ștefan Dumitra")),
                        new Credit("Sunet", List.of("Dorel Sidorof, Mihai Pop")),
                        new Credit("Video", List.of("Costi Șimon", "Sebastian Gherman")),
                        new Credit("Regia tehnică", List.of("Paul Tănase"))
                ))
                .build();

        String casaDeLaTaraUrl = "https://images.unsplash.com/photo-1759864979680-b5dbb5d80f7f";
        Performance casa = Performance.builder()
                .title("Casa de la țară")
                .director("Claudiu Goga")
                .location("Sala Studio")
                .theaterName(tnb)
                .startDateTime(LocalDateTime.of(2026, 11, 4, 19, 0))
                .coverImageURL(getCoverImageUrl(casaDeLaTaraUrl))
                .ageLimit(6)
                .duration(180)
                .fullCoverImageURL(getFullCoverImageUrl(casaDeLaTaraUrl))
                .purchaseTicketLink("https://www.tnb.ro/ro/casa-de-la-tara")
                .description("\"Cu umor și ironie, dar și cu o luciditate împinsă până la cruzime, Donald Margulies (deținător al Premiului Pulitzer pentru dramaturgie) propune o complicată poveste de familie care naște multe întrebări și nu oferă nici un răspuns.\n" +
                        "Casa de la țară- o poveste despre ce trăim atunci când trăim. Ne trăim viața? Ne trăim cariera? Ne trăim visele? Ne trăim eșecurile?\n" +
                        "Casa de la țară – o poveste despre mai multe feluri de a iubi și despre mai multe feluri de a urî.\n" +
                        "Casa de la țară – o poveste despre forța de distrugere a unui sărut și despre forța de distrugere a lipsei unui sărut.\n" +
                        "Casa de la țară – o poveste despre greutatea cuvintelor spuse și despre durerea cuvintelor nespuse.\n" +
                        "Casa de la țară – o poveste despre prețul succesului și prețul ratării.\n" +
                        "Casa de la țară – o poveste despre arta pură și arta comercială.\n" +
                        "Casa de la țară – o poveste despre demonii orgoliului și demonii pasiunii.\n" +
                        "Casa de la țară – o poveste despre părinți și copii.\n" +
                        "Casa de la țară – o poveste despre oameni.\"Claudiu Goga")
                .creditList(List.of(
                        new Credit("Regia", List.of("Claudiu Goga")),
                        new Credit("Ilustrație muzicală", List.of("Claudiu Goga")),
                        new Credit("Asistent regie", List.of("Patricia Katona")),
                        new Credit("Video design", List.of("Constantin Simon")),
                        new Credit("Light design", List.of("Bogdan Golumbeanu", "Ion Vlașcu")),
                        new Credit("Regia tehnică", List.of("Silviu Negulete"))
                ))
                .build();

        String ceiDreptiUrl = "https://images.unsplash.com/photo-1503443062224-9f77d743cf25";
        Performance ceiDrepti = Performance.builder()
                .title("Cei drepți")
                .director("Mihai Măniuțiu")
                .location("Sala Atelier")
                .theaterName(tnb)
                .startDateTime(LocalDateTime.of(2026, 10, 22, 19, 0))
                .coverImageURL(getCoverImageUrl(ceiDreptiUrl))
                .ageLimit(14)
                .duration(90)
                .fullCoverImageURL(getFullCoverImageUrl(ceiDreptiUrl))
                .purchaseTicketLink("https://www.tnb.ro/ro/casa-de-la-tara")
                .description("Prin Cei drepți (1949), Albert Camus a creat o operă cu ecouri puternice în contemporaneitate, chestionând granițele fragile dintre dreptate și crimă, precum și legitimitatea violenței politice. Scrisă în cheia unei tragedii shakespeariene, piesa urmărește dilemele morale ale unui grup de tineri din Partidul Socialist Revoluționar. În Moscova lui februarie 1905, aceștia pun la cale un atentat împotriva ducelui Serghei, unchiul țarului.\n" +
                        "\n" +
                        "Confruntarea ideologică din interiorul grupului aduce în prim-plan două viziuni radical opuse asupra revoluției: pe de o parte, extremismul rece al lui Stepan, care transformă moartea într-un cult; pe de altă parte, idealismo-sensibilitatea unor ucigași „delicați” precum Kaliaiev și Dora, care ucid nu din ură, ci pentru a clădi o lume în care nimeni să nu mai sufere. Prinși într-un angrenaj în care dragostea pare imposibilă, cei doi tineri își găsesc împlinirea iubirii doar dincolo de viață.\n" +
                        "\n" +
                        "Alegând omul în detrimentul dogmei și pledând pentru măsură împotriva nihilismului, spectacolul pus în scenă de Mihai Măniuțiu la Teatrul Național București devine o meditație profundă asupra iubirii, pierderii inocenței și catastrofei morale.\n" +
                        "\n" +
                        "Cuvântul regizorului (Mihai Măniuțiu):\n" +
                        "\n" +
                        "„Cei drepți este o piesă poliedrică, un amestec de foc și gheață. O poveste despre o dragoste frântă de istorie, despre iluziile otrăvite ale terorismului, sacrificiu, violență și frumusețe pură. O călătorie dureroasă spre centrul unui labirint în care lumina se confundă cu sfâșierea. Un asemenea parcurs n-ar fi fost posibil fără dăruirea extraordinară a actorilor: Marius Manole, Raluca Aprodu, Marius Bodochi, Mirela Oprișor, Maia Morgenstern și Ciprian Nicula. Așteptăm cu nerăbdare întâlnirea cu publicul.”")
                .creditList(List.of(
                        new Credit("Regia", List.of("Mihai Măniuțiu")),
                        new Credit("Decor", List.of("Adrian Damian")),
                        new Credit("Costume", List.of("Luiza Enescu")),
                        new Credit("Muzica", List.of("Mihai Dobre")),
                        new Credit("Regia tehnică", List.of("Andi Tuinea"))
                ))
                .build();

        List<Performance> ps = List.of(
                minuneaMinunilor,
                orlando,
                livada,
                incendii,
                casaBernardei,
                logodna,
                jocuri,
                indoiala,
                amintiri,
                casa,
                ceiDrepti
        );

        List<Performance> notAdded = ps.stream()
                .filter((p) -> !performanceRepository.existsByTitle(p.getTitle()))
                .toList();

        if (!notAdded.isEmpty()) {
            performanceRepository.saveAll(notAdded);
        }

    }

    private String getCoverImageUrl(String url) {
        return url + "?w=800&h=600&fit=crop&auto=format";
    }

    private String getFullCoverImageUrl(String url) {
        return url + "?w=1600&h=1200&fit=crop&auto=format";
    }
}
