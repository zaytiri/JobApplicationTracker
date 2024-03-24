import React from "react";
import { JobTracker } from "./job_tracker";

import { createRoot } from 'react-dom/client';
const container = document.getElementById('root');
const root = createRoot(container);
root.render(<JobTracker/>);