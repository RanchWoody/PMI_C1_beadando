import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Scanner;






public class Main {

    public static void main(String[] args) {

        menu();

    }

    public static void menu()
    {
        Scanner be = new Scanner(System.in);
        boolean kilepes = true;

        //while hogy folyamatos legyen a menü és csak a kilépés után záródjon be a program
        while (kilepes)
        {
            try
            {
                //Az XML fájl előkészítése olvasásra
                File inputFile = new File("orak.xml");
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(inputFile);

                //Menü
                System.out.println("\t--- MENÜ ---\n1 - Órák megnézése\n2 - Új óra hozzáadása\n3 - Meglévő óra szerkesztése\n4 - Óra törlése\n0 - Kilépés");
                switch(be.nextInt())
                {
                    case 0:
                        System.out.println("\nA program hamarosan bezárul!");

                        try
                        {
                            //a kilépés előtt időzítő indul a könnyebb olvashatóság érdekében
                            Thread.sleep(2500);
                            kilepes = false;
                        }
                        catch (InterruptedException e)
                        {
                            System.out.println("HIBA!");
                        }
                        break;
                    case 1:

                        System.out.println("");

                        //a tantárgyak kiiratása teljes szövegként, külön sorokban
                        for (int i = 1; i < doc.getDocumentElement().getChildNodes().getLength(); i+=2)
                        {
                            NodeList asd = doc.getDocumentElement().getChildNodes().item(i).getChildNodes();

                            System.out.print("A tárgy neve "+asd.item(1).getTextContent()+", a tanár "+asd.item(3).getTextContent()+", a heti óraszám "+asd.item(5).getTextContent());
                            if("Igen".equals(asd.item(7).getTextContent()))
                            {
                                System.out.println(", kötelező.");
                            }
                            else if("Nem".equals(asd.item(7).getTextContent()))
                            {
                                System.out.println(", nem kötelező.");
                            }
                        }

                        System.out.println("\n\n");

                        break;
                    case 2:
                        be.nextLine();
                        Element root = doc.getDocumentElement();
                        Element ujOra = doc.createElement("ora");

                        //Külön elementek készítése melyek megegyeznek az eredetivel
                        Element nev = doc.createElement("nev");
                        Element tanar = doc.createElement("tanar");
                        Element hetiDB = doc.createElement("hetiDarab");
                        Element kotelezo = doc.createElement("kotelezo");

                        //az elementek feltöltése adattal
                        System.out.println("Kérem adja meg a tantárgy nevét: ");
                        nev.appendChild(doc.createTextNode(be.nextLine()));

                        System.out.println("Kérem adja meg a tanár nevét: ");
                        tanar.appendChild(doc.createTextNode(be.nextLine()));

                        System.out.println("Kérem adja meg hetente hány ilyen óra van: ");
                        hetiDB.appendChild(doc.createTextNode(String.valueOf(be.nextInt())));

                        //mivel egy boolean adat így egy switch case-el csak az igen vagy nem opciót lehet választani
                        System.out.println("Kötelező az óra?\n1 - Igen\n2 - Nem");
                        boolean vege = true;
                        while (vege)
                        {
                            switch (be.nextInt())
                            {
                                case 1:
                                    kotelezo.appendChild(doc.createTextNode("Igen"));
                                    vege = false;
                                    break;
                                case 2:
                                    kotelezo.appendChild(doc.createTextNode("Nem"));
                                    vege = false;
                                    break;
                                default:
                                    System.out.println("Hibás adat!");
                            }
                        }

                        //az új órába ágyazom a már meglévő elementeket, így tartalmazza mindegyik szükséges tulajdonságot
                        ujOra.appendChild(nev);
                        ujOra.appendChild(tanar);
                        ujOra.appendChild(hetiDB);
                        ujOra.appendChild(kotelezo);

                        root.appendChild(ujOra);

                        //az xml elmentése illetve a formázása
                        Transformer transformer = TransformerFactory.newInstance().newTransformer();
                        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                        Result output = new StreamResult(new File("orak.xml"));
                        Source input = new DOMSource(root);

                        transformer.transform(input, output);
                        break;
                    case 3:

                        be.nextLine();

                        System.out.println("Kérem adja meg a szerkesztendő óra nevét: ");
                        String szerkNev = be.nextLine();
                        System.out.println("Illetve a tanárát: ");
                        String szertTan = be.nextLine();
                        boolean voltE = true;

                        for (int i = 1; i < doc.getDocumentElement().getChildNodes().getLength(); i+=2)
                        {
                            NodeList asd = doc.getDocumentElement().getChildNodes().item(i).getChildNodes();

                            if(asd.item(1).getTextContent().equals(szerkNev) && asd.item(3).getTextContent().equals(szertTan))
                            {
                                voltE = false;

                                System.out.println("Az új név: ");
                                asd.item(1).setTextContent(be.nextLine());

                                System.out.println("Az új tanár: ");
                                asd.item(3).setTextContent(be.nextLine());

                                System.out.println("Az új heti darabszám: ");
                                asd.item(5).setTextContent(String.valueOf(be.nextInt()));

                                System.out.println("Kötelező?\n1 - Igen\n2 - Nem");
                                vege = true;
                                while (vege)
                                {
                                    switch (be.nextInt())
                                    {
                                        case 1:
                                            asd.item(7).setTextContent("Igen");
                                            vege = false;
                                            break;
                                        case 2:
                                            asd.item(7).setTextContent("Nem");
                                            vege = false;
                                            break;
                                        default:
                                            System.out.println("Hibás adat!");
                                    }
                                }

                            }
                        }

                        if(voltE)
                        {
                            System.out.println("Nincs ilyen óra!");
                        }

                        Transformer tf = TransformerFactory.newInstance().newTransformer();
                        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                        tf.setOutputProperty(OutputKeys.INDENT, "yes");
                        Result out = new StreamResult(new File("orak.xml"));
                        Source in = new DOMSource(doc);

                        tf.transform(in, out);

                        break;
                    case 4:
                        break;
                    default:
                        System.out.println("\nHibás választás!\n");
                }
            }
            catch (Exception e)
            {
                System.out.println(e);
            }


        }
    }
}
