import './config/env';
import app from './app';
import { connectDB } from './config/db';

const port = Number(process.env.PORT ?? 8083);

connectDB()
    .then(() => {
        app.listen(port, () => {
            console.log(`user-service running on port ${port}`);
        });
    })
    .catch((error) => {
        console.error('Failed to start user-service', error);
        process.exit(1);
    });
