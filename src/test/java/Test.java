/**
 * Copyright 2010 Sina Bagherzadeh
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Sina Bagherzadeh
 */
public class Test {
    public static void main(String[] args) {
        float a = 1;
        for (int i = 0;i < 100;i++) {
            a *= 0.5F;
            System.out.println("a = " + a);
        }
            
    }
}
