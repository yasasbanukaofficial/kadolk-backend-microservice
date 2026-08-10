import { NextFunction, Request, Response } from 'express';
import { z, ZodTypeAny } from 'zod';

export const validate = (schemas: { body?: ZodTypeAny; params?: ZodTypeAny; query?: ZodTypeAny }) => {
    return (req: Request, res: Response, next: NextFunction) => {
        try {
            if (schemas.params) {
                schemas.params.parse(req.params);
            }
            if (schemas.query) {
                schemas.query.parse(req.query);
            }
            if (schemas.body) {
                req.body = schemas.body.parse(req.body);
            }
            next();
        } catch (error) {
            if (error instanceof z.ZodError) {
                const messages = error.issues.map((issue) => issue.message);
                return res.status(400).json({ statusCode: 400, message: 'Validation Failed', data: messages });
            }
            next(error);
        }
    };
};
