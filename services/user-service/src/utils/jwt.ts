import jwt from 'jsonwebtoken';

export const signToken = (userId: unknown): string => {
    const secret = process.env.JWT_SECRET ?? 'your-jwt-secret';
    return jwt.sign({ userId }, secret, { expiresIn: '1d' });
};
