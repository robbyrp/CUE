export const ROUTES = {
    HOME: '/',
    EXPLOREAZA: '/exploreaza',
    LOGIN: '/login',
    REGISTER: '/register',
    PROFIL: '/profil/vizualizare',
    ADAUGA_SPECTACOL: '/spectacole/adauga',
    CREEAZA_SPECTACOL: '/spectacole/adauga',
    SPECTACOL: (id: number | string) => `/spectacole/${id}`,
};