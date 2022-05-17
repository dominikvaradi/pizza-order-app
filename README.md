# Pizzarendelő webalkalmazás

## [Mockupok](./MOCKUPS.md)

## Specifikáció

Egy olyan pizzarendelő webalkalmazást szeretnénk készíteni, ahol az oldalra látogatók meg tudják tekinteni a kínálatot, pizzát tudnak rendelni kizárólag házhozszállítással.

A felhasználóknak lehetőségük van megtekinteni a régebbi rendeléseiket, módosítani tudják az adataikat és nyomon tudják követni az aktív rendelésük állapotát. Pizzarendeléshez az oldalra látogatóknak be kell jelentkezniük a felhasználójukba, ha pedig nincsen felhasználójuk akkor regisztrálniuk kell egyet. Bejelentkezés nélkül lehetőségük van megtekinteni az oldalra látogatóknak a pizza-kínálatot, de ők rendelni és egyéb tevékenységet nem tudnak csinálni.

Sima mezei felhasználókón kívül vannak adminisztrátorok, szakácsok és üzletvezetők is. Az adminisztrátorok látják az összes felhasználót a rendszerben, és módosítani tudják az adataikat. Az adminisztrátorok és az üzletvezetők módosítani tudják a pizza kínálatot, hozzá tudnak adni új pizzákat és törölni is tudnak a meglévők közül. Az adminisztrátorok, üzletvezetők és szakácsok látják az aktív rendeléseket, azok állapotát módosítani tudják. Az adminisztrátorok, üzletvezetők látják az összes (beleértve a régebbi, lezárt) rendelést.

Egy rendeléshez legalább egy pizza (de akár több pizza is) tartozik. Egy felhasználónak egyszerre csak egy aktív rendelése lehet. A rendelésekhez tartozik egy kiszállítási cím is, ami általános esetben megegyezik a felhasználó tartózkodási címével is, de lehetősége van a felhasználónak más címet is megadnia rendelésnél. A rendelésnél a felhasználó kiválaszthatja, hogy készpénzzel, vagy kártyával fizet, de fizetnie csak is a helyszínen, átvételkor lehetséges. Egy pizzához eltároljuk a nevét, egy képet, az árát és egy leírást, amiben a tartalmazott hozzávalókat sorolhatjuk fel. A felhasználóknak eltároljuk a felhasználónevét, jelszavát, email címét. Egy felhasználóhoz több cím is tartozhat, ezeket külön tudja fel venni. Egy rendelésnek öt állapota lehet: Feldolgozás alatt, Elkészítés alatt, Kiszállítás alatt, Visszavonva, Teljesítve. A felhasználóknak lehetőségük van visszavonni aktív rendelésüket a “Feldolgozás alatt” állapotban, viszont utána ez már nem lehetséges. Az alkalmazás az “Elkészítés alatt” állapotban elkészít egy számlát a rendeléshez, és kiküldi azt a felhasználó email címére.
