#!/usr/bin/env bash

#
# Copyright 2020 New Vector Ltd
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# Template prevent from upgrading Android Studio, so this script de configure the template
echo "Un-configure Element Template..."
if [ -z ${ANDROID_STUDIO+x} ]; then ANDROID_STUDIO="/Applications/Android Studio.app/Contents"; fi

rm "${ANDROID_STUDIO%/}/plugins/android/lib/templates/other/ElementFeature"
rm -r "${ANDROID_STUDIO%/}/plugins/android/lib/templates"
