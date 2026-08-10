import { Response } from 'express';

export const successResponse = <T>(res: Response, statusCode: number, message: string, data: T): void => {
    res.status(statusCode).json({ statusCode, message, data });
};
