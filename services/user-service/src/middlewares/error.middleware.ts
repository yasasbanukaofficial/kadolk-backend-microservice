import { NextFunction, Request, Response } from 'express';
import { z } from 'zod';

export const notFoundHandler = (req: Request, res: Response) => {
    res.status(404).json({ statusCode: 404, message: 'Route not found', data: null });
};

export const errorHandler = (error: unknown, req: Request, res: Response, next: NextFunction) => {
    if (error instanceof z.ZodError) {
        const messages = error.issues.map((issue) => issue.message);
        return res.status(400).json({ statusCode: 400, message: 'Validation Failed', data: messages });
    }

    if (error instanceof Error && typeof (error as Error & { statusCode?: number }).statusCode === 'number') {
        return res.status((error as Error & { statusCode: number }).statusCode)
            .json({ statusCode: (error as Error & { statusCode: number }).statusCode, message: error.message, data: null });
    }

    if (error instanceof Error && (error as Error & { code?: number }).code === 11000) {
        return res.status(409).json({ statusCode: 409, message: 'User already exists with this email', data: null });
    }

    if (error instanceof Error && error.name === 'CastError') {
        return res.status(404).json({ statusCode: 404, message: 'User not found', data: null });
    }

    res.status(500).json({ statusCode: 500, message: 'An unexpected error occurred', data: null });
};
