import './config/env';
import app from './app';
import { connectDB } from './config/db';
import { loadRemoteConfig } from './config/remoteConfig';

async function start(): Promise<void> {
    await loadRemoteConfig();

    const port = Number(process.env.PORT ?? 8005);

    await connectDB();
    app.listen(port, () => {
        console.log(`user-service running on port ${port}`);
    });
}

start().catch((error) => {
    console.error('Failed to start user-service', error);
    process.exit(1);
});