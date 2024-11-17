"use client";

import PacketStream from "./PacketStream/PacketStream";

export default function MyApp() {
  return (
    <div className="grid grid-rows-[auto_1fr_auto] justify-items-center min-h-screen p-8 pb-10 gap-8 sm:p-10 font-[family-name:var(--font-geist-sans)]">
      <header className="row-start-1 w-full bg-gray-600 text-white p-4 shadow-lg">
        <div className="text-3xl font-bold text-center">aprj2 Dashboard</div>
      </header>
      <main className="flex flex-col gap-8 row-start-2 items-start w-full">
        <div className="flex gap-4 w-full">
          <PacketStream />
        </div>
      </main>
      <footer className="row-start-3 flex gap-4 flex-wrap items-center justify-center">
        <a href="https://github.com/xdab/aprj2" className="text-blue-600">GitHub</a>
      </footer>
    </div>
  );
}
