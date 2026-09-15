import { jwtDecode } from 'jwt-decode';
import type { JwtPayload } from 'jwt-decode';
import type { UserRole } from '../types/UserRole';

interface AppJwtPayload extends JwtPayload {
    role: string;
}

const SPRING_ROLE_PREFIX = 'ROLE_';

export const decodeUserRole = (token: string): UserRole | null => {
    try {
        const payload = jwtDecode<AppJwtPayload>(token);
        const rawRole = payload.role.startsWith(SPRING_ROLE_PREFIX)
            ? payload.role.slice(SPRING_ROLE_PREFIX.length)
            : payload.role;
        return rawRole as UserRole;
    } catch {
        return null;
    }
};
