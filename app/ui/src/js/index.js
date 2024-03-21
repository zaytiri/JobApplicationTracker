import React from "react";
import { MyComponent } from "./api_calls";

import { createRoot } from 'react-dom/client';
const container = document.getElementById('root');
const root = createRoot(container);
root.render(<MyComponent/>);
