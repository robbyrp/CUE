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
        String theater = "Teatrul de Stat Constanta";

        String minuneaMinunilorUrl = "https://images.unsplash.com/photo-1503095396549-807759245b35";
        Performance minuneaMinunilor = Performance.builder()
                .title("Minunea minunilor")
                .director("DANIEL CHIRILĂ")
                .location("Scena Deschisă")
                .theaterName(theater)
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
                .theaterName(theater)
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
                .theaterName(theater)
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
                .theaterName(theater)
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
                .theaterName(theater)
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
                .theaterName(theater)
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
                .theaterName(theater)
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
                .theaterName(theater)
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

        performanceRepository.saveAll(List.of(
                minuneaMinunilor,
                orlando,
                livada,
                incendii,
                casaBernardei,
                logodna,
                jocuri,
                indoiala
        ));
    }

    private String getCoverImageUrl(String url) {
        return url + "?w=800&h=600&fit=crop&auto=format";
    }

    private String getFullCoverImageUrl(String url) {
        return url + "?w=1600&h=1200&fit=crop&auto=format";
    }
}
