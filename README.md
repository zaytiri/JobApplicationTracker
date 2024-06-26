# JobTrackr - Job Application Tracker

[//]: # (INTRO GIF HERE)

## Table of Contents

- [Description](#description)
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
- [Support](#support)
- [License](#license)
- [Status](#status)
- [Shoutout](#shoutout)

<a name="description"></a>
## Description

A simple desktop app that allows you to easily track job applications by creating different status and viewing relevant statistics.

Job applications can also easily be added by providing the URL of the offer and all relevant information will be processed automatically into the app.

### Gallery for UI

<img src="https://github.com/zaytiri/JobApplicationTracker/blob/main/readme-imgs/1.png" width="700" height="400" />
<img src="https://github.com/zaytiri/JobApplicationTracker/blob/main/readme-imgs/2.png" width="700" height="400" /><br>
<img src="https://github.com/zaytiri/JobApplicationTracker/blob/main/readme-imgs/3.png" width="700" height="400" />
<img src="https://github.com/zaytiri/JobApplicationTracker/blob/main/readme-imgs/4.png" width="700" height="400" /><br>
<img src="https://github.com/zaytiri/JobApplicationTracker/blob/main/readme-imgs/5.png" width="700" height="400" />
<img src="https://github.com/zaytiri/JobApplicationTracker/blob/main/readme-imgs/6.png" width="700" height="400" /><br>
<img src="https://github.com/zaytiri/JobApplicationTracker/blob/main/readme-imgs/7.png" width="700" height="400" />
<img src="https://github.com/zaytiri/JobApplicationTracker/blob/main/readme-imgs/8.png" width="700" height="400" /><br>

<a name="features"></a>
## Features

| Status | Feature                                     |
|:-------|:--------------------------------------------|
| ✅      | Add, view and remove job applications         |
| ✅      | Add, view and remove job statuses         |
| ✅      | Auto fill job information through offer's URL         |
| ✅      | Auto update job status through offer's URL         |
| ✅      | View activity roadmap for each job application         |
| ✅      | View statistics         |
| ❌      | Add, view, remove and sort job applications by custom tags        |
| ❌      | Add, view and remove documents to each job application        |
| ❌      | Generate Sankeymaic Diagram Input based on statistics and status        |


_Disclaimer: The order does not necessarily describe the priority of development of each feature which the status corresponds to ❌._

Any new features are **_very_** welcomed. If you need some feature or you have any suggestion, open an issue and I can analyse if it's feasible.

### Auto fill feature
When adding a new job offer, by providing first the specific URL of the job offer, the program can scrape the link for all the relevant information to create a new job offer in the system.

Currently, the feature supports the following platforms with the respective URL structure that the system supports:
- LinkedIn: "https://www.linkedin.com/jobs/view/<JOB_UID>"
- GlassDoor: "https://www.glassdoor.com/Job/<JOB_UID>" or "https://www.glassdoor.com/job-listing/<JOB_UID"
- Xing: "https://www.xing.com/jobs/<JOB_UID>"

(Note: The Tootltip in this feature will say that currently it only works for LinkedIn, but that's not the case. Will be updated in the next release.)

### Database
The app stores all persistent information in a local database which should be available in the following folder:
    `C:\Users\<yourusername>\jobtracker\data` containing a file named `jobtracker.db`.

This file contains all information and if deleted or modified, the saved information in the app could be either deleted or corrupted, respectively.

### Future features
All future features are documented in the above table with a ❌ icon. More can be added with time.

<a name="prerequisites"></a>
## Prerequisites

To run this program you will need the following:
- [JDK 17 Download](https://www.oracle.com/java/technologies/downloads/#jdk17-windows) ([x64 Installer (for Windows)](https://download.oracle.com/java/17/latest/jdk-17_windows-x64_bin.exe) recommended)

The JDK 17 is necessary to run .jar executable files. For now, the version cannot be higher than 17, due to compiled version files (more info [here](https://javaalmanac.io/bytecode/versions/)).

<a name="installation"></a>
## Installation

After downloading the latest executable file available in the [Releases](https://github.com/zaytiri/JobApplicationTracker/releases) page, right-click the jobtrackr_setup.exe and follow the instructions from the installer.

1. Installing should take a few minutes due to some third party libraries but this is planned to be reduced in the upcoming releases.

<a name="support"></a>
## Support

1. If you successfully installed the app but when trying to add a job offer or add a new status, if the popup does not disappear and the button appears to not do anything this could mean that you do not meet the JDK 17 requirement. Please check again if the JDK version you have for java is 17. You can also open a Command Prompt and input the following command: `java -version` 

If any issues/problems are encountered, please feel free to open an issue.

<a name="license"></a>
## License

[MIT](https://choosealicense.com/licenses/mit/)

<a name="status"></a>
## Status

Currently, fully maintaining it.

<a name="shoutout"></a>
## Shoutout

The UI was fully based on this [free template](https://www.creative-tim.com/product/argon-dashboard-chakra) made by Creative Tim.