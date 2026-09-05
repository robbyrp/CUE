export const ROUTES = {
    HOME: '/',
    LOGIN: '/login',
    PROFIL: '/profil/vizualizare',
    ADAUGA_SPECTACOL: '/spectacole/adauga',
    CREEAZA_SPECTACOL: '/spectacole/adauga',
    SPECTACOL: (id: number | string) => `/spectacole/${id}`,
};